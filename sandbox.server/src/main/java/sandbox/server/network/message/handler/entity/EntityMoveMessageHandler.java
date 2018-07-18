package sandbox.server.network.message.handler.entity;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Move;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;

public class EntityMoveMessageHandler implements MessageHandler<EntityMoveMessage> {

	@Override
	public void handle(Connection connection, EntityMoveMessage received) {
		Entity player = GameServer.INSTANCE.getPlayer(connection);
		WorldEntityComponent wec = (WorldEntityComponent) player.getComponent(WorldEntityComponent.ID);
		received.attachment = new Move(player.getUUID(), received.attachment.getCardinalOrientation());
		player.trigger(received);
	}
}
