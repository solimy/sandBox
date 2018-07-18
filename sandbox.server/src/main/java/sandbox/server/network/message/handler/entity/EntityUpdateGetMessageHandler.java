package sandbox.server.network.message.handler.entity;

import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;

public class EntityUpdateGetMessageHandler implements MessageHandler<EntityUpdateGetMessage> {

	@Override
	public void handle(Connection connection, EntityUpdateGetMessage received) {
		Entity entity = World.INSTANCE.entityManager.get(received.attachment);
		if (entity != null) {
			connection.send(Messages.ENTITY_UPDATE.build(entity));
		}
	}

}
