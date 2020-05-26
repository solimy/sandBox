package sandbox.common.world.model;

import java.util.Map;
import sandbox.engine.misc.UUID;
import java.util.concurrent.ConcurrentHashMap;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.world.enums.BiomeType;
import sandbox.common.world.enums.TerrainType;
import sandbox.engine.game.Entity;

public class Cell {
	private final Map<UUID, Entity> dynamicEntities = new ConcurrentHashMap<>();
	private Coordinates coordinates;
	private BiomeType biomeType;
	private TerrainType terrainType;
	public boolean isWalkable;
	
	public Cell(Coordinates coordinates, BiomeType biomeType, TerrainType terrainType) {
		this.coordinates = coordinates;
		this.biomeType = biomeType;
		this.terrainType = terrainType;
		this.isWalkable = terrainType.isWalkable;
	}
	
	public void copyTerrain(Cell toCopy) {
		this.coordinates = toCopy.coordinates;
		this.biomeType = toCopy.biomeType;
		this.terrainType = toCopy.terrainType;
		this.isWalkable = terrainType.isWalkable;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public BiomeType getBiomeType() {
		return biomeType;
	}

	public TerrainType getTerrainType() {
		return terrainType;
	}

	public boolean isWalkable() {
		return isWalkable;
	}
	
	public Map<UUID, Entity> getDynamicEntities() {
		return dynamicEntities;
	}
}
