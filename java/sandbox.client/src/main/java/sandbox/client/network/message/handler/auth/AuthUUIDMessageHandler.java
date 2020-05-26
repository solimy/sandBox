package sandbox.client.network.message.handler.auth;

import sandbox.client.ClientScript;
import sandbox.client.state.StateImpl;
import sandbox.common.protocol.messages.auth.AuthUUIDMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.engine.logging.Logger;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.RawMessage;

public enum AuthUUIDMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		AuthUUIDMessage uuid = new AuthUUIDMessage(message);
		ClientScript.INSTANCE.uuid = uuid.uuid;
		ClientScript.INSTANCE.connection
				.send(new EntityUpdateGetMessage(ClientScript.INSTANCE.uuid));
		ClientScript.INSTANCE.stateManager.setState(StateImpl.CONNECTED);
		Logger.INSTANCE.debug("Client UUID : {" + ClientScript.INSTANCE.uuid + "}");
	}
}
