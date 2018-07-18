package sandbox.client.game.render;

import sandbox.client.resources.Ressources;
import sandbox.common.world.enums.TerrainType;
import sandbox.engine.graphic.drawable.sprite.Sprite;

public class TerrainRenderer {
	
	public static Sprite getSprite(TerrainType terrainType) {
		switch (terrainType) {
		case GRASS:
			return Ressources.INSTANCE.SPRITE_TERRAIN_GRASS; 
		case GRASS_WATER1:
			return Ressources.INSTANCE.SPRITE_TERRAIN_GRASS_WATER1; 
		case GRASS_WATER2:
			return Ressources.INSTANCE.SPRITE_TERRAIN_GRASS_WATER2; 
		case GRASS_DIRT:
			return Ressources.INSTANCE.SPRITE_TERRAIN_GRASS_DIRT; 
		default:
			return Ressources.INSTANCE.SPRITE_ENTITY_FLOWER;
		}
	}
}
