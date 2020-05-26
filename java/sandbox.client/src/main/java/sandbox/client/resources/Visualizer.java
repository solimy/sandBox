package sandbox.client.resources;

import java.lang.reflect.InvocationTargetException;

import sandbox.engine.Engine;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.drawable.sprite.Sprite;

class Visualizer {

	enum Render implements Script<Void> {
		INSTANCE;

		Sprite TOMBSTONE = Ressources.INSTANCE.FILE_terrain_atlas.extractSprite(15 * 32, 21 * 32, 32, 64).setSize(500, 1000);

		@Override
		public void execute(Void unused) {
			GraphicApplication.INSTANCE.pixelUnit = 100;
			GraphicApplication.INSTANCE.render(Ressources.INSTANCE.SPRITE_TERRAIN_GRASS_WATER1);
			GraphicApplication.INSTANCE.render(TOMBSTONE.setOrigin(0,0));
			if ((Engine.Clock.INSTANCE.getCurrentTimeMillis() / 1000) % 2 == 0)
				GraphicApplication.INSTANCE.render(TOMBSTONE.setOrigin(50, -10));
		}
	}


	public static void main(String[] args) {
		GraphicApplication.INSTANCE.init("Visualize", 200, 200);
		GraphicApplication.INSTANCE.setFramesPerSecond(10L);
		GraphicApplication.INSTANCE.setOnResizeScript(Script.EMPTY);
		GraphicApplication.INSTANCE.setOnRenderScript(Render.INSTANCE);
		try {
			GraphicApplication.INSTANCE.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
