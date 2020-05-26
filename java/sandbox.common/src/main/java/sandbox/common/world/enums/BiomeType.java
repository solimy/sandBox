package sandbox.common.world.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public enum BiomeType {
	FOREST(0, TerrainType.GRASS, TerrainType.GRASS_DIRT, TerrainType.GRASS_WATER1, TerrainType.GRASS_WATER2);

	private static final Map<Integer, BiomeType> biomeTypes = new HashMap<>();
	static {
		for (BiomeType biomeType : BiomeType.values())
			biomeTypes.put(biomeType.ordinal(), biomeType);
	}
	
	public final Vector<TerrainType> possibleTerrains = new Vector<>();
	public final Integer rarity;

	private BiomeType(Integer rarity, TerrainType... types) {
		this.rarity = rarity;
		for (TerrainType type : types)
			possibleTerrains.addElement(type);
	}

	public static BiomeType getByOridinal(Integer oridinal) {
		return biomeTypes.get(oridinal);
	}
}
