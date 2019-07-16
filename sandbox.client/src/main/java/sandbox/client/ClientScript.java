package sandbox.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import sandbox.client.game.KeysHandlerScript;
import sandbox.client.game.WindowResizeScript;
import sandbox.client.game.render.CameraScript;
import sandbox.client.network.message.ClientMessagesHandlersLoader;
import sandbox.client.state.StateManagerImpl;
import sandbox.common.protocol.CommonSerializablesLoader;
import sandbox.common.protocol.messages.auth.AuthConnectMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.logging.Logger;
import sandbox.engine.misc.Token;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.AsyncNIOClient;
import sandbox.engine.network.Connection;

public enum ClientScript {
	INSTANCE;

	public final StateManagerImpl stateManager;
	public final AsyncNIOClient client;
	public volatile Connection connection = null;
	public Entity playerEntity = null;
	public UUID uuid = null;

	private ClientScript() {
		stateManager = new StateManagerImpl();
		AsyncNIOClient client = null;
		try {
			client = new AsyncNIOClient(
					connection -> {
						this.connection = connection;
						Logger.INSTANCE.debug("Client connected to server.");
					},
					connection -> {
						this.connection = null;
						Logger.INSTANCE.debug("Client disconnected from the server.");
					}				
			);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.client = client;
		this.client.connect("localhost", 8983);
	}

	public static void main(String[] args) {
		CommonSerializablesLoader.load();
		ClientMessagesHandlersLoader.load();
		World.INSTANCE.entityManager.createChunkIfNotPresent(false);
		World.INSTANCE.entityManager.askStateManagerBeforeMove(false);
		ClientScript.INSTANCE.connection.send(new AuthConnectMessage(new Token()));
		GraphicApplication.INSTANCE.init("Sandbox Graphic Client", 300, 200);
		GraphicApplication.INSTANCE.setFramesPerSecond(30L);
		GraphicApplication.INSTANCE.setOnKeyPressedScript(KeysHandlerScript.INSTANCE);
		GraphicApplication.INSTANCE.setOnResizeScript(WindowResizeScript.INSTANCE);
		GraphicApplication.INSTANCE.setOnRenderScript(CameraScript.INSTANCE);
		try {
			GraphicApplication.INSTANCE.start();
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
