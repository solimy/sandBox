package sandbox.client.network.message.handler.entity;

import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum EntityDeathMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		EntityDeathMessage deathMessage = new EntityDeathMessage(message);
		Entity entity = (Entity) World.INSTANCE.entityManager.get(deathMessage.uuid);
		if (entity != null)
			entity.trigger(deathMessage);
	}
}
