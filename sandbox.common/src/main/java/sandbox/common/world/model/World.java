package sandbox.common.world.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Spawner;
import sandbox.engine.math.CardinalOrientation;

public enum World {
	INSTANCE;
	private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>>> chunks = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<UUID, Entity> dynamicEntities = new ConcurrentHashMap<>();

	private Chunk newChunk(Coordinates coordinates) {
		Integer x = coordinates.getChunkX(), y = coordinates.getChunkY(), z = coordinates.getLayer();
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>> layer = null;
		ConcurrentHashMap<Integer, Chunk> row = null;
		Chunk chunk = null;

		if (coordinates != null) {
			layer = chunks.get(z);
			if (layer != null) {
				row = layer.get(y);
				if (row != null) {
					chunk = row.get(x);
					if (chunk == null) {
						chunk = row.computeIfAbsent(x, (k) -> {
							Chunk newChunk = new Chunk();
							newChunk.coordinates.copy(coordinates);
							Chunk.Generate(this, newChunk, System.nanoTime(), x, y, z);
							Chunk.Populate(this, newChunk, System.nanoTime());
							return newChunk;
						});
					}
				} else {
					row = layer.computeIfAbsent(y, (k) -> {
						return new ConcurrentHashMap<>();
					});
					chunk = row.computeIfAbsent(x, (k) -> {
						Chunk newChunk = new Chunk();
						newChunk.coordinates.copy(coordinates);
						Chunk.Generate(this, newChunk, System.nanoTime(), x, y, z);
						Chunk.Populate(this, newChunk, System.nanoTime());
						return newChunk;
					});
				}
			} else {
				layer = chunks.computeIfAbsent(z, (k) -> {
					return new ConcurrentHashMap<>();
				});
				row = layer.computeIfAbsent(y, (k) -> {
					return new ConcurrentHashMap<>();
				});
				chunk = row.computeIfAbsent(x, (k) -> {
					Chunk newChunk = new Chunk();
					newChunk.coordinates.copy(coordinates);
					Chunk.Generate(this, newChunk, System.nanoTime(), x, y, z);
					Chunk.Populate(this, newChunk, System.nanoTime());
					return newChunk;
				});
			}
		}
		return chunk;
	}

	public Chunk getChunk(Integer x, Integer y, Integer z) {
		return getChunk(new Coordinates(Chunk.WIDTH, Chunk.LENGTH).setChunkCoordinates(x, y, 0, 0, z));
	}

	public Chunk getChunk(Coordinates coordinates) {
		Integer x = coordinates.getChunkX(), y = coordinates.getChunkY(), z = coordinates.getLayer();
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>> layer = null;
		ConcurrentHashMap<Integer, Chunk> row = null;
		Chunk chunk;

		layer = chunks.get(z);
		if (layer != null) {
			row = layer.get(y);
			if (row != null) {
				chunk = row.get(x);
				if (chunk == null) {
					chunk = newChunk(coordinates);
				}
			} else {
				chunk = newChunk(coordinates);
			}
		} else {
			chunk = newChunk(coordinates);
		}
		return chunk;
	}

	public Chunk getNullChunk(Coordinates coordinates) {
		Integer x = coordinates.getChunkX(), y = coordinates.getChunkY(), z = coordinates.getLayer();
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>> layer = null;
		ConcurrentHashMap<Integer, Chunk> row = null;
		Chunk chunk = null;

		layer = chunks.get(z);
		if (layer != null) {
			row = layer.get(y);
			if (row != null) {
				chunk = row.get(x);
			}
		}
		return chunk == null || !chunk.isGenerated ? null : chunk;
	}

	public Cell getCell(Coordinates coordinates) {
		Chunk chunk = null;
		Cell cell = null;

		chunk = getChunk(coordinates);
		cell = chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()];
		return cell;
	}

	public Cell getNullCell(Coordinates coordinates) {
		Chunk chunk = null;
		Cell cell = null;

		chunk = getNullChunk(coordinates);
		if (chunk != null) {
			cell = chunk.cells[coordinates.getInChunkY()][coordinates.getInChunkX()];
		}
		return cell;
	}

	synchronized public void putChunk(Coordinates coordinates, Chunk chunk) {
		Integer x = coordinates.getChunkX(), y = coordinates.getChunkY(), z = coordinates.getLayer();
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>> layer = null;
		ConcurrentHashMap<Integer, Chunk> row = null;
		Chunk existingChunk;

		if (coordinates != null) {
			layer = chunks.get(z);
			if (layer == null) {
				layer = new ConcurrentHashMap<>();
				chunks.put(z, layer);
			}
			row = layer.get(y);
			if (row == null) {
				row = new ConcurrentHashMap<>();
				layer.put(y, row);
			}
			existingChunk = row.get(x);
			if (existingChunk != null) {
				existingChunk.dynamicEntities.forEach((uuid, entity) -> dynamicEntities.remove(uuid));
			}
			chunk.dynamicEntities.forEach((uuid, entity) -> dynamicEntities.put(uuid, entity));
			row.put(x, chunk);
		}
	}

	synchronized public void putChunkTerrain(Coordinates coordinates, Chunk chunk) {
		Integer x = coordinates.getChunkX(), y = coordinates.getChunkY(), z = coordinates.getLayer();
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Chunk>> layer = null;
		ConcurrentHashMap<Integer, Chunk> row = null;
		Chunk existingChunk;

		if (coordinates != null) {
			layer = chunks.get(z);
			if (layer == null) {
				layer = new ConcurrentHashMap<>();
				chunks.put(z, layer);
			}
			row = layer.get(y);
			if (row == null) {
				row = new ConcurrentHashMap<>();
				layer.put(y, row);
			}
			existingChunk = row.get(x);
			if (existingChunk != null) {
				for (int i = 0; i < Chunk.LENGTH; ++i) {
					for (int j = 0; j < Chunk.WIDTH; ++j) {
						existingChunk.cells[i][j].copyTerrain(chunk.cells[i][j]);
					}
				}
			} else {
				putChunk(coordinates, chunk);
			}
			row.put(x, chunk);
		}
	}

	public Long getTotalChunksCount() {
		Long[] totalChunkCount = { 0L };
		chunks.forEach((z, layer) -> layer.forEach((y, row) -> row.forEach((x, chunk) -> ++totalChunkCount[0])));
		return totalChunkCount[0];
	}

	public Long getTotalDynamicEntitiesCount() {
		Long[] totalDynamicEntitiesCount = { 0L };
		chunks.forEach((z, layer) -> layer.forEach((y, row) -> row
				.forEach((x, chunk) -> chunk.dynamicEntities.forEach((id, entity) -> ++totalDynamicEntitiesCount[0]))));
		return totalDynamicEntitiesCount[0];
	}

	public EntityManager entityManager = new EntityManager();

	public class EntityManager {
		private Boolean createChunkIfNotPresent = null;
		public void createChunkIfNotPresent(Boolean value) {
			createChunkIfNotPresent = value;
		}
		private Boolean askStateManagerBeforeMove = null;
		public void askStateManagerBeforeMove(Boolean value) {
			askStateManagerBeforeMove = value;
		}
		public Entity spawn(Spawner spawner) {
			Entity entity = spawner.spawn(UUID.randomUUID());
			put(((WorldEntityComponent)entity.getComponent(WorldEntityComponent.ID)).getPosition().get().coordinates, entity, true);
			return entity;
		}

		public Entity get(UUID uuid) {
			return dynamicEntities.get(uuid);
		}

		public void removeEntity(UUID uuid) {
			Entity entity = dynamicEntities.get(uuid);
			dynamicEntities.remove(uuid);
			if (entity != null) {
				WorldEntityComponent wes = (WorldEntityComponent) entity.getComponent(WorldEntityComponent.ID);
				Chunk chunk = getNullChunk(wes.getPosition().get().coordinates);
				if (chunk != null) {
					chunk.dynamicEntities.remove(entity.getUUID());
					chunk.cells[wes.getPosition().get().coordinates
							.getInChunkY()][wes.getPosition().get().coordinates.getInChunkX()]
									.getDynamicEntities().remove(entity.getUUID());
				}
			}
		}

		public void removeEntity(Entity entity) {
			removeEntity(entity.getUUID());
		}

		public void put(Coordinates coordinates, Entity entity, Boolean createChunkIfNotPresent) {
			if ((!createChunkIfNotPresent && getNullCell(coordinates) == null) || !getCell(coordinates).isWalkable)
				return;
			Entity existingEntity = get(entity.getUUID());
			if (existingEntity == null)
				existingEntity = entity;
			removeEntity(existingEntity);
			WorldEntityComponent wes = (WorldEntityComponent) existingEntity.getComponent(WorldEntityComponent.ID);
			wes.getPosition().get().coordinates.copy(coordinates);
			dynamicEntities.put(existingEntity.getUUID(), existingEntity);
			Chunk chunk = getChunk(wes.getPosition().get().coordinates);
			chunk.dynamicEntities.put(existingEntity.getUUID(), existingEntity);
			chunk.cells[wes.getPosition().get().coordinates
					.getInChunkY()][wes.getPosition().get().coordinates.getInChunkX()]
							.getDynamicEntities().put(existingEntity.getUUID(), existingEntity);
		}

		public Entity move(CardinalOrientation move, Entity entity) {
			WorldEntityComponent wes = (WorldEntityComponent) entity.getComponent(WorldEntityComponent.ID);
			Coordinates coordinates = new Coordinates(wes.getPosition().get().coordinates);
			if (askStateManagerBeforeMove && !wes.getStateManager().get().setStateIsAvailable()) {
				return null;
			}
			if (wes.getPosition().get().orientation != move) {
				wes.getPosition().get().orientation = move;
				return entity;
			}
			switch (move) {
			case NORTH:
				coordinates.setWorldY(coordinates.getWorldY() + 1);
				if (!getCell(coordinates).isWalkable)
					return null;
				removeEntity(entity);
				put(coordinates, entity, createChunkIfNotPresent);
				break;
			case SOUTH:
				coordinates.setWorldY(coordinates.getWorldY() - 1);
				if (!getCell(coordinates).isWalkable)
					return null;
				removeEntity(entity);
				put(coordinates, entity, createChunkIfNotPresent);
				break;
			case WEST:
				coordinates.setWorldX(coordinates.getWorldX() - 1);
				if (!getCell(coordinates).isWalkable)
					return null;
				removeEntity(entity);
				put(coordinates, entity, createChunkIfNotPresent);
				break;
			case EAST:
				coordinates.setWorldX(coordinates.getWorldX() + 1);
				if (!getCell(coordinates).isWalkable)
					return null;
				removeEntity(entity);
				put(coordinates, entity, createChunkIfNotPresent);
				break;
			default:
				break;
			}
			return entity;
		}
	}

	public Collection<Entity> getDynamicEntities() {
		return dynamicEntities.values();
	}
}
