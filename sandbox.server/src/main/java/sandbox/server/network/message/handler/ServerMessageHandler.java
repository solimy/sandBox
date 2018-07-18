package sandbox.server.network.message.handler;

import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.auth.AuthConnectMessage;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;
import sandbox.server.network.message.handler.auth.AuthConnectMessageHandler;
import sandbox.server.network.message.handler.chunk.ChunkEntitiesGetMessageHandler;
import sandbox.server.network.message.handler.chunk.ChunkTerrainGetMessageHandler;
import sandbox.server.network.message.handler.entity.EntityMoveMessageHandler;
import sandbox.server.network.message.handler.entity.EntityUpdateGetMessageHandler;
import sandbox.server.network.message.handler.entity.EntityUseActionMessageHandler;

public class ServerMessageHandler implements MessageHandler<Message<?, ?>> {
	private final AuthConnectMessageHandler authConnectMessageHandler;
	private final ChunkTerrainGetMessageHandler chunkTerrainGetMessageHandler;
	private final ChunkEntitiesGetMessageHandler chunkEntitiesGetMessageHandler;
	private final EntityUpdateGetMessageHandler entityUpdateGetMessageHandler;
	private final EntityMoveMessageHandler entityMoveMessageHandler;
	private final EntityUseActionMessageHandler entityUseActionMessageHandler;

	public ServerMessageHandler() {
		authConnectMessageHandler = new AuthConnectMessageHandler();
		chunkTerrainGetMessageHandler = new ChunkTerrainGetMessageHandler();
		chunkEntitiesGetMessageHandler = new ChunkEntitiesGetMessageHandler();
		entityUpdateGetMessageHandler = new EntityUpdateGetMessageHandler();
		entityMoveMessageHandler = new EntityMoveMessageHandler();
		entityUseActionMessageHandler = new EntityUseActionMessageHandler();
	}

	@Override
	public void handle(Connection connection, Message<?, ?> message) {
		Messages messageType = Messages.getByMessageCode(message.type);

		System.out.println("[" + System.currentTimeMillis() + "] received : " + messageType.name());
		switch (messageType) {
		case PING:
			connection.send(Messages.PONG.build(null));
			break;
		case PONG:
			connection.send(Messages.PING.build(null));
			break;
		case CHUNK_TERRAIN_GET:
			chunkTerrainGetMessageHandler.handle(connection, (ChunkTerrainGetMessage) message);
			break;
		case CHUNK_ENTITIES_GET:
			chunkEntitiesGetMessageHandler.handle(connection, (ChunkEntitiesGetMessage) message);
			break;
		case AUTH_CONNECT:
			authConnectMessageHandler.handle(connection, (AuthConnectMessage) message);
			break;
		case ENTITY_UPDATE_GET:
			entityUpdateGetMessageHandler.handle(connection, (EntityUpdateGetMessage) message);
			break;
		case ENTITY_MOVE:
			entityMoveMessageHandler.handle(connection, (EntityMoveMessage) message);
			break;
		case ENTITY_USE_ACTION:
			entityUseActionMessageHandler.handle(connection, (EntityUseActionMessage) message);
			break;
		default:
			break;
		}
	}
}
