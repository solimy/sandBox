package sandbox.client.resources;

import java.util.Arrays;

import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.MainScript;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.math.Vector2D;

class Visualizer implements MainScript<Visualizer> {

	enum Render implements Script<Visualizer> {
		INSTANCE;

		Vector2D spritePos = new Vector2D(15, 10);
		Animation animaion = new Animation(1000L,
				Arrays.asList(Ressources.INSTANCE.FILE_terrain_atlas.extractSprite(15 * 32, 21 * 32, 32, 64)));

		@Override
		public void execute(Visualizer context) {
			animaion.getFrame(0L).reset().setFit(100, 100).render(GraphicApplication.getGraphicsContext());
			animaion.getFrame(0L).reset().setOrigin(16,32).render(GraphicApplication.getGraphicsContext());
		}
	}

	@Override
	public void execute(GraphicApplication<Visualizer> context) {
		context.setFramesPerSecond(10L);
		context.setOnResizeScript((Script<GraphicApplication>) Script.EMPTY);
		context.setOnRenderScript(Render.INSTANCE);
	}

	public static void main(String[] args) {
		GraphicApplication.lauchGraphicApplication(new Visualizer());
	}
}
