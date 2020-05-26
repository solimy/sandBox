package sandbox.client.network.message.handler.entity;

import sandbox.common.protocol.messages.entity.EntityRemoveMessage;
import sandbox.common.world.model.World;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum EntityRemoveMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		EntityRemoveMessage removeMessage = new EntityRemoveMessage(message);
		World.INSTANCE.entityManager.removeEntity(removeMessage.uuid);
	}
}
