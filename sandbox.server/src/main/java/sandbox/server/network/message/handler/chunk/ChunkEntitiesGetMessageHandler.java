package sandbox.server.network.message.handler.chunk;

import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;

public class ChunkEntitiesGetMessageHandler implements MessageHandler<ChunkEntitiesGetMessage> {

	@Override
	public void handle(Connection connection, ChunkEntitiesGetMessage received) {
		Chunk chunk = World.INSTANCE.getChunk(received.attachment);
		chunk.dynamicEntities.forEach((k, v) -> connection.send(Messages.ENTITY_UPDATE.build(v)));
	}

}
