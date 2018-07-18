package sandbox.server.network.message.handler.auth;

import java.util.UUID;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.auth.AuthConnectMessage;
import sandbox.common.world.enums.EntityNature;
import sandbox.common.world.model.Chunk;
import sandbox.engine.game.Entity;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;
import sandbox.server.game.components.RPGComponent;
import sandbox.server.game.components.ServerEntityComponent;
import sandbox.server.game.items.FireballItem;

public class AuthConnectMessageHandler implements MessageHandler<AuthConnectMessage> {


	@Override
	public void handle(Connection connection, AuthConnectMessage received) {
		// TODO auth verification

		Entity player = GameServer.INSTANCE.getPlayer(connection);
		WorldEntityComponent wec = new WorldEntityComponent(
				new Position(new Coordinates(Chunk.WIDTH, Chunk.LENGTH), CardinalOrientation.SOUTH),
				EntityNature.PLAYER);
		ServerEntityComponent sec = new ServerEntityComponent(wec.getStateManager(), wec.getPosition());
		RPGComponent ic = new RPGComponent(0, 150, 150, 150);
		ic.getItemShortcut().get().putSlot(new Entity(UUID.randomUUID()).addComponent(FireballItem.ID, new FireballItem()),
				0);
		player.addComponent(WorldEntityComponent.ID, wec).addComponent(ServerEntityComponent.ID, sec).addComponent(RPGComponent.ID, ic);
		connection.send(Messages.AUTH_UUID.build(player.getUUID()));
		player.create();
	}
}
