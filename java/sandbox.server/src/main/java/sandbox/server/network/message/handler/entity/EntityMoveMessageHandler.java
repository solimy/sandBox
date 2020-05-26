package sandbox.server.network.message.handler.entity;

import sandbox.common.game.events.Move;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;
import sandbox.server.game.GameServer;

public enum EntityMoveMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage rawMessage) {
		EntityMoveMessage received = new EntityMoveMessage(rawMessage);
		Entity player = GameServer.INSTANCE.getPlayer(connection);
		Move move = new Move(player.getUUID(), received.move.getCardinalOrientation());
		received = new EntityMoveMessage(move);
		player.trigger(received);
	}
}
