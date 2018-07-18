package sandbox.server.network.message.handler.entity;

import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;

public class EntityUseActionMessageHandler implements MessageHandler<EntityUseActionMessage> {

	@Override
	public void handle(Connection connection, EntityUseActionMessage received) {
		Entity player = GameServer.INSTANCE.getPlayer(connection);
		player.trigger(received);
	}
}
