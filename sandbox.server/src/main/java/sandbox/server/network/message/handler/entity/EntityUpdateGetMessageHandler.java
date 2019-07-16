package sandbox.server.network.message.handler.entity;

import java.util.stream.Collectors;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.logging.Logger;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum EntityUpdateGetMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage rawMessage) {
		EntityUpdateGetMessage received = new EntityUpdateGetMessage(rawMessage);
		Entity entity = World.INSTANCE.entityManager.get(received.uuid);
		Logger.INSTANCE.debug("EntityUpdateGetMessageHandler::handle : entity="+entity);
		if (entity != null) {
			UUID uuid = entity.getUUID();
			WorldEntityComponent worldEntityComponent = (WorldEntityComponent) entity.getComponent(WorldEntityComponent.ID);
			String entityName = entity.getName();
			connection.send(new EntityUpdateMessage(uuid, worldEntityComponent, entityName));
		}
	}

}
