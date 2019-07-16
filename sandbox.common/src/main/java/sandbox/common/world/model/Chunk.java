package sandbox.common.world.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import sandbox.engine.misc.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import sandbox.common.math.position.Coordinates;
import sandbox.common.world.enums.BiomeType;
import sandbox.common.world.enums.TerrainType;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Spawner;
import sandbox.engine.logging.Logger;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;
import sandbox.engine.network.serialization.Serializer;

public class Chunk implements Serializable {
	public static final int TYPE = Chunk.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		Chunk chunk = new Chunk();
		chunk.coordinates.copy((Coordinates) Serializer.deserializeNext(buffer));
		for (int i = 0; i < Chunk.LENGTH; i++) {
			for (int j = 0; j < Chunk.WIDTH; j++) {
				int biomeTypeOrdinal = buffer.getInt();
				int terrainTypeOrdinal = buffer.getInt();
				BiomeType biomeType = BiomeType.getByOridinal(biomeTypeOrdinal);
				TerrainType terrainType = TerrainType.getByOrdinal(terrainTypeOrdinal);
				Coordinates coordinates = new Coordinates(Chunk.WIDTH, Chunk.LENGTH)
						.setWorldCoordinates(buffer.getInt(), buffer.getInt(), buffer.getInt());
				chunk.cells[i][j] = new Cell(coordinates, biomeType, terrainType);
			}
		}
		chunk.isGenerated = true;
		return chunk;
	};
	
	public static final Integer WIDTH = 8;
	public static final Integer LENGTH = 8;

	private final AtomicLong lastUpdate = new AtomicLong(0L);
	public final Cell[][] cells = new Cell[LENGTH][WIDTH];
	public boolean isGenerated = false;
	public final Map<UUID, Entity> dynamicEntities = new ConcurrentHashMap<>();
	public final Coordinates coordinates = new Coordinates(Chunk.WIDTH, Chunk.LENGTH);

	public static void Populate(World worldMap, Chunk chunk, Long seed) {
		Random random = new Random();
		random.setSeed(seed);
		for (int y = 0; y < LENGTH; ++y) {
			for (int x = 0; x < WIDTH; ++x) {
				random.setSeed(random.nextLong());
				Cell cell = chunk.cells[y][x];
				if (cell.getDynamicEntities().size() == 0 && cell.getTerrainType().spawners.size() > 0
						&& random.nextBoolean()) {
					Spawner spawner = cell.getTerrainType().spawners
							.get(random.nextInt(cell.getTerrainType().spawners.size()));
					worldMap.entityManager.put(cell.getCoordinates(), spawner.spawn(new UUID()), true);
				}
			}
		}
	}

	public static Chunk Generate(World worldMap, Chunk chunk, Long seed, Integer chunkX, Integer chunkY,
			Integer layer) {
		Random random = new Random();
		BiomeType biomeType = null;
		TerrainType terrainType = null;
		Coordinates coordinates = new Coordinates(Chunk.WIDTH, Chunk.LENGTH);
		List<Cell> neighbours = null;
		Cell neighbour = null;
		Integer wX = coordinates.getWorldX(), wY = coordinates.getWorldY();
		random.setSeed(seed);
		for (int y = 0; y < LENGTH; ++y) {
			for (int x = 0; x < WIDTH; ++x) {
				random.setSeed(random.nextLong());
				neighbours = new ArrayList<>();
				coordinates.setWorldCoordinates(wX + 1, wY, layer);
				neighbour = coordinates.getChunkX() == chunkX && coordinates.getChunkY() == chunkY
						? chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()]
						: worldMap.getNullCell(coordinates);
				if (neighbour != null)
					neighbours.add(neighbour);
				coordinates.setWorldCoordinates(wX - 1, wY, layer);
				neighbour = coordinates.getChunkX() == chunkX && coordinates.getChunkY() == chunkY
						? chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()]
						: worldMap.getNullCell(coordinates);
				if (neighbour != null)
					neighbours.add(neighbour);
				coordinates.setWorldCoordinates(wX, wY + 1, layer);
				neighbour = coordinates.getChunkX() == chunkX && coordinates.getChunkY() == chunkY
						? chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()]
						: worldMap.getNullCell(coordinates);
				if (neighbour != null)
					neighbours.add(neighbour);
				coordinates.setWorldCoordinates(wX, wY - 1, layer);
				neighbour = coordinates.getChunkX() == chunkX && coordinates.getChunkY() == chunkY
						? chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()]
						: worldMap.getNullCell(coordinates);
				if (neighbour != null)
					neighbours.add(neighbour);
				if (neighbours.isEmpty()) {
					Logger.INSTANCE.debug(new Date(System.currentTimeMillis()).toString() + ": " + Thread.currentThread()
							+ ":\n=> Chunk.Generate: no neighbours found at (" + chunkX + "," + chunkY + ")");
					biomeType = BiomeType.values()[random.nextInt(BiomeType.values().length)];
					terrainType = biomeType.possibleTerrains.get(random.nextInt(biomeType.possibleTerrains.size()));
				} else {
					neighbour = neighbours.get(random.nextInt(neighbours.size()));
					if (random.nextInt(100) <= 95) {
						biomeType = neighbour.getBiomeType();
						if (random.nextInt(100) <= 95) {
							terrainType = neighbour.getTerrainType();
						} else {
							terrainType = biomeType.possibleTerrains
									.get(random.nextInt(biomeType.possibleTerrains.size()));
						}
					} else {
						biomeType = BiomeType.values()[random.nextInt(BiomeType.values().length)];
						terrainType = biomeType.possibleTerrains.get(random.nextInt(biomeType.possibleTerrains.size()));
					}
				}
				chunk.cells[y][x] = new Cell(
						new Coordinates(coordinates).setChunkCoordinates(chunkX, chunkY, x, y, layer), biomeType,
						terrainType);
			}
		}
		chunk.isGenerated = true;
		// Logger.INSTANCE.debug(
		// "world size : " + worldMap.getTotalChunksCount() + " chunks, " +
		// worldMap.getTotalDynamicEntitiesCount()
		// + " dynamic entities");
		return chunk;
	}

	public Cell getCell(Coordinates coordinates) {
		return cells[coordinates.getInChunkY()][coordinates.getInChunkX()];
	}

	public Cell getCell(Integer x, Integer y) {
		return cells[y][x];
	}

	public void update(Long currentTimeMillis) {
		if (lastUpdate.getAndSet(currentTimeMillis) == currentTimeMillis)
			return;
		dynamicEntities.values().parallelStream().forEach(Entity::update);
	}

	@Override
	public ByteBuffer encodePayload() {
		ByteBuffer buffer = ByteBuffer.allocate(0);
		ByteBuffer chunkCoorinates = Serializer.serialize(this.coordinates);
		buffer = ByteBuffer
				.allocate(buffer.capacity() + chunkCoorinates.capacity() + (Chunk.LENGTH * Chunk.WIDTH * Integer.BYTES * 5))
				.put(chunkCoorinates);
		for (int i = 0; i < Chunk.LENGTH; i++) {
			for (int j = 0; j < Chunk.WIDTH; j++) {
				Cell cell = cells[i][j];
				Coordinates coordinates = cell.getCoordinates();
				buffer
					.putInt(cell.getBiomeType().ordinal())
					.putInt(cell.getTerrainType().ordinal())
					.putInt(coordinates.getWorldX())
					.putInt(coordinates.getWorldY())
					.putInt(coordinates.getLayer());
			}
		}
		return buffer;
	}

	@Override
	public Integer getType() {
		return TYPE;
	}

}
