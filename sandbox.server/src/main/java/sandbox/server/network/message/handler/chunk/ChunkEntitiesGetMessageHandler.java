package sandbox.server.network.message.handler.chunk;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum ChunkEntitiesGetMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage rawMessage) {
		ChunkEntitiesGetMessage received = new ChunkEntitiesGetMessage(rawMessage);
		Chunk chunk = World.INSTANCE.getChunk(received.coordinates);
		chunk.dynamicEntities.forEach((k, entity) -> {
			UUID uuid = entity.getUUID();
			WorldEntityComponent worldEntityComponent = (WorldEntityComponent) entity.getComponent(WorldEntityComponent.ID);
			String entityName = entity.getName();
			connection.send(new EntityUpdateMessage(uuid, worldEntityComponent, entityName));
		});
	}

}
