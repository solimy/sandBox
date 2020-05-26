package sandbox.client.network.message;

import sandbox.client.network.message.handler.auth.AuthUUIDMessageHandler;
import sandbox.client.network.message.handler.chunk.ChunkTerrainMessageHandler;
import sandbox.client.network.message.handler.entity.EntityDamagedMessageHandler;
import sandbox.client.network.message.handler.entity.EntityDeathMessageHandler;
import sandbox.client.network.message.handler.entity.EntityMoveMessageHandler;
import sandbox.client.network.message.handler.entity.EntityRemoveMessageHandler;
import sandbox.client.network.message.handler.entity.EntityUpdateMessageHandler;
import sandbox.common.protocol.messages.auth.AuthUUIDMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainMessage;
import sandbox.common.protocol.messages.entity.EntityDamagedMessage;
import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityRemoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.engine.network.message.MessageHandlingService;

public final class ClientMessagesHandlersLoader {
	public static void load() {
		MessageHandlingService.INSTANCE.register(AuthUUIDMessage.TYPE, AuthUUIDMessageHandler.INSTANCE);		
		MessageHandlingService.INSTANCE.register(ChunkTerrainMessage.TYPE, ChunkTerrainMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityDeathMessage.TYPE, EntityDeathMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityDamagedMessage.TYPE, EntityDamagedMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityMoveMessage.TYPE, EntityMoveMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityRemoveMessage.TYPE, EntityRemoveMessageHandler.INSTANCE);
		MessageHandlingService.INSTANCE.register(EntityUpdateMessage.TYPE, EntityUpdateMessageHandler.INSTANCE);
	}
}
