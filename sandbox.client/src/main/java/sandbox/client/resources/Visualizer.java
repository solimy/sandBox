package sandbox.client.resources;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.MainScript;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.math.Vector2D;

class Visualizer implements MainScript<Visualizer> {

	enum Render implements Script<GraphicApplication<Visualizer>> {
		INSTANCE;

		Vector2D spritePos = new Vector2D(15, 10);
		Animation animaion = new Animation(1000L,
				Arrays.asList(Ressources.INSTANCE.FILE_terrain_atlas.extractSprite(15 * 32, 21 * 32, 32, 64)));

		@Override
		public void execute(GraphicApplication<Visualizer> context) {
			context.render(animaion.getFrame(0L).reset().fit(100, 100));
			context.render(animaion.getFrame(0L).reset().setOrigin(16,32));
		}
	}

	@Override
	public void execute(GraphicApplication<Visualizer> context) {
		context.setFramesPerSecond(10L);
		context.setOnResizeScript((Script<GraphicApplication<Visualizer>>) Script.EMPTY);
		context.setOnRenderScript(Render.INSTANCE);
	}

	public static void main(String[] args) {
		var gA = new GraphicApplication<Visualizer>(new Visualizer());
		try {
			gA.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
