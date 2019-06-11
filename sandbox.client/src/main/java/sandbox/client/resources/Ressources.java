package sandbox.client.resources;

import sandbox.engine.filesystem.SpriteSheet;
import sandbox.engine.graphic.drawable.sprite.Sprite;

public enum Ressources {
	INSTANCE();

	public final SpriteSheet FILE_notFound;
	public final SpriteSheet FILE_terrain_atlas;
	public final SpriteSheet FILE_obj_misk_atlas;
	public final SpriteSheet FILE_skeleton;
	public final SpriteSheet FILE_fireball;
	public final SpriteSheet FILE_explosion;
	public final SpriteSheet FILE_aks;

	public final Sprite SPRITE_NOT_FOUND;

	public final Sprite SPRITE_TERRAIN_GRASS;
	public final Sprite SPRITE_TERRAIN_GRASS_WATER1;
	public final Sprite SPRITE_TERRAIN_GRASS_WATER2;
	public final Sprite SPRITE_TERRAIN_GRASS_DIRT;

	public final Sprite SPRITE_ENTITY_FLOWER;
	public final Sprite SPRITE_ENTITY_ROCK;

	public final Sprite SPRITE_ENTITY_PUMPKIN;

	private Ressources() {
		FILE_notFound = new SpriteSheet(Ressources.class, "/assets/notFound.png");		
		FILE_terrain_atlas = new SpriteSheet(Ressources.class, "/assets/terrain_atlas.png");
		FILE_obj_misk_atlas = new SpriteSheet(Ressources.class, "/assets/obj_misk_atlas.png");
		FILE_skeleton = new SpriteSheet(Ressources.class, "/assets/skeleton.png");
		FILE_fireball = new SpriteSheet(Ressources.class, "/assets/fireball_0.png");
		FILE_explosion = new SpriteSheet(Ressources.class, "/assets/exp2_0.png");
		FILE_aks = new SpriteSheet(Ressources.class, "/assets/aks_assets.png");
		
		SPRITE_NOT_FOUND = FILE_explosion.extractSprite();
		SPRITE_TERRAIN_GRASS = FILE_terrain_atlas.extractSprite(32 * 22, 32 * 5, 32, 32);
		SPRITE_TERRAIN_GRASS_WATER1 = FILE_terrain_atlas.extractSprite(32 * 6, 32 * 9, 32, 32);
		SPRITE_TERRAIN_GRASS_WATER2 = FILE_terrain_atlas.extractSprite(32 * 6, 32 * 10, 32, 32);
		SPRITE_TERRAIN_GRASS_DIRT = FILE_terrain_atlas.extractSprite(32 * 5, 32 * 15, 32, 32);

		SPRITE_ENTITY_FLOWER = FILE_terrain_atlas.extractSprite(32 * 6, 32 * 31, 32, 32);
		SPRITE_ENTITY_ROCK = FILE_terrain_atlas.extractSprite(32 * 29, 32 * 25, 32, 32);

		SPRITE_ENTITY_PUMPKIN = FILE_obj_misk_atlas.extractSprite(32 * 21, 32 * 5, 32, 32);
	}
}
