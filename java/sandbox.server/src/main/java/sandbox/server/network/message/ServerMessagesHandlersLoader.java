package sandbox.server.network.message;

import sandbox.common.protocol.messages.auth.AuthConnectMessage;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.server.network.message.handler.auth.AuthConnectMessageHandler;
import sandbox.server.network.message.handler.chunk.ChunkEntitiesGetMessageHandler;
import sandbox.server.network.message.handler.chunk.ChunkTerrainGetMessageHandler;
import sandbox.server.network.message.handler.entity.EntityMoveMessageHandler;
import sandbox.server.network.message.handler.entity.EntityUpdateGetMessageHandler;
import sandbox.server.network.message.handler.entity.EntityUseActionMessageHandler;

public final class ServerMessagesHandlersLoader {
	public static void load() {
		MessageHandlingService.INSTANCE.register(AuthConnectMessage.TYPE, AuthConnectMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(ChunkEntitiesGetMessage.TYPE, ChunkEntitiesGetMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(ChunkTerrainGetMessage.TYPE, ChunkTerrainGetMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityMoveMessage.TYPE, EntityMoveMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityUpdateGetMessage.TYPE, EntityUpdateGetMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityUseActionMessage.TYPE, EntityUseActionMessageHandler.INSTANCE);
	}
}
