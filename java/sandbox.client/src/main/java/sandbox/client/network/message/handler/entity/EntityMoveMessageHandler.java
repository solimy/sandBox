package sandbox.client.network.message.handler.entity;

import sandbox.client.ClientScript;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum EntityMoveMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		EntityMoveMessage move = new EntityMoveMessage(message);
		Entity movingEntity = World.INSTANCE.entityManager.get(move.move.getMovingUUID());
		if (movingEntity == null) {
			ClientScript.INSTANCE.connection
					.send(new EntityUpdateGetMessage(move.move.getMovingUUID()));
			return;
		}
		movingEntity.trigger(move);
	}
}
