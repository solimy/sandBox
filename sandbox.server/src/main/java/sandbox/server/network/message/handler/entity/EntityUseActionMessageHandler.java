package sandbox.server.network.message.handler.entity;

import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;
import sandbox.server.game.GameServer;

public enum EntityUseActionMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage rawMessage) {
		EntityUseActionMessage received = new EntityUseActionMessage(rawMessage);
		Entity player = GameServer.INSTANCE.getPlayer(connection);
		player.trigger(received);
	}
}
