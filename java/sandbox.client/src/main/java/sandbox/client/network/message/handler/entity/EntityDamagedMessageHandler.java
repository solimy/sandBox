package sandbox.client.network.message.handler.entity;

import sandbox.common.protocol.messages.entity.EntityDamagedMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.RawMessage;

public enum EntityDamagedMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		EntityDamagedMessage entityDamagesMessage = new EntityDamagedMessage(message);
		Entity entity = (Entity) World.INSTANCE.entityManager.get(entityDamagesMessage.uuid);
		if (entity != null)
			entity.trigger(entityDamagesMessage);
	}
}
