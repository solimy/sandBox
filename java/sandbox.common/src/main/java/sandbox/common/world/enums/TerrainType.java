package sandbox.common.world.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sandbox.engine.game.Spawner;

public enum TerrainType {
	//
	GRASS(90, Arrays.asList(), Arrays.asList(), true),
	//
	GRASS_DIRT(20, Arrays.asList(), Arrays.asList(), true),
	//
	GRASS_WATER1(20, Arrays.asList(), Arrays.asList(), true),
	//
	GRASS_WATER2(20, Arrays.asList(), Arrays.asList(), true),
	//
	;

	private static final Map<Integer, TerrainType> terrainTypes = new HashMap<>();
	static {
		for (TerrainType terrainType : TerrainType.values())
			terrainTypes.put(terrainType.ordinal(), terrainType);
	}

	public final List<EntityNature> possibleScenery;
	public final List<Spawner> spawners;
	public final Integer rarity;
	public final boolean isWalkable;

	private TerrainType(Integer rarity, List<EntityNature> sceneries, List<Spawner> dynamicEntitiesSuppliers,
			boolean isWalkable) {
		this.rarity = rarity;
		this.possibleScenery = sceneries;
		this.spawners = dynamicEntitiesSuppliers;
		this.isWalkable = true;
	}

	public static TerrainType getByOrdinal(Integer ordinal) {
		return terrainTypes.get(ordinal);
	}
}
