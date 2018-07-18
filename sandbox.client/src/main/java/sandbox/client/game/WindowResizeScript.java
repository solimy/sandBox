package sandbox.client.game;

import sandbox.client.game.render.CameraScript;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;

public enum WindowResizeScript implements Script<GraphicApplication> {
	INSTANCE;
	
	@Override
	public void execute(GraphicApplication context) {
		 CameraScript.INSTANCE.updateSize();
	}
}
