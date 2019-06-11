package sandbox.client.resources;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.math.Vector2D;

class Visualizer {

	enum Render implements Script<Void> {
		INSTANCE;

		Vector2D spritePos = new Vector2D(15, 10);
		Animation animaion = new Animation(1000L,
				Arrays.asList(Ressources.INSTANCE.FILE_terrain_atlas.extractSprite(15 * 32, 21 * 32, 32, 64)));

		@Override
		public void execute(Void unused) {
			GraphicApplication.INSTANCE.render(animaion.getFrame(0L).reset().fit(100, 100));
			GraphicApplication.INSTANCE.render(animaion.getFrame(0L).reset().setOrigin(16,32));
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
