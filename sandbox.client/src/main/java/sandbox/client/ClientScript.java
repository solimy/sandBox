package sandbox.client;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import sandbox.client.game.KeysHandlerScript;
import sandbox.client.game.WindowResizeScript;
import sandbox.client.game.render.CameraScript;
import sandbox.client.network.NetworkManager;
import sandbox.client.state.StateManagerImpl;
import sandbox.common.misc.Token;
import sandbox.common.protocol.Messages;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.MainScript;

public enum ClientScript implements MainScript<ClientScript> {
	INSTANCE;

	public final StateManagerImpl stateManager;
	public final NetworkManager networkManager;
	public Entity playerEntity = null;
	public UUID uuid = null;
	public GraphicApplication<ClientScript> context = null;

	private ClientScript() {
		stateManager = new StateManagerImpl();
		networkManager = new NetworkManager();
		networkManager.connect("localhost", 8983);
	}

	public static void main(String[] args) {
		try {
			new GraphicApplication(INSTANCE).start();
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void execute(GraphicApplication<ClientScript> context) {
		World.INSTANCE.entityManager.createChunkIfNotPresent(false);
		World.INSTANCE.entityManager.askStateManagerBeforeMove(false);
		ClientScript.INSTANCE.context = context;
		ClientScript.INSTANCE.networkManager.connection.send(Messages.AUTH_CONNECT.build(new Token()));
		context.setFramesPerSecond(30L);
		context.setOnKeyPressedScript(KeysHandlerScript.INSTANCE);
		context.setOnResizeScript(WindowResizeScript.INSTANCE);
		context.setOnRenderScript(CameraScript.INSTANCE);
		//CameraScript.INSTANCE.updateSize();
	}
}
