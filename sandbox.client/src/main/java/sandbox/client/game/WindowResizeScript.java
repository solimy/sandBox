package sandbox.client.game;

import javax.swing.JFrame;

import sandbox.client.ClientScript;
import sandbox.client.game.render.CameraScript;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;

public enum WindowResizeScript implements Script<Void> {
	INSTANCE;
	
	@Override
	public void execute(Void unused) {
		JFrame frame = GraphicApplication.INSTANCE.getFrame();
		CameraScript.INSTANCE.updateSize(frame.getWidth(), frame.getWidth());
	}
}
