package sandbox.common.world.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.world.enums.BiomeType;
import sandbox.common.world.enums.TerrainType;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Spawner;

public class Chunk {
	private final AtomicLong lastUpdate = new AtomicLong(0L);
	public boolean isGenerated = false;
	public static final Integer WIDTH = 8;
	public static final Integer LENGTH = 8;
	public final Cell[][] cells = new Cell[LENGTH][WIDTH];
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
					worldMap.entityManager.put(cell.getCoordinates(), spawner.spawn(UUID.randomUUID()), true);
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
					System.out.println(new Date(System.currentTimeMillis()).toString() + ": " + Thread.currentThread()
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
		// System.out.println(
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

}
