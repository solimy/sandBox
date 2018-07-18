package sandbox.server.network.message.handler.chunk;

import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;

public class ChunkTerrainGetMessageHandler implements MessageHandler<ChunkTerrainGetMessage> {

	@Override
	public void handle(Connection connection, ChunkTerrainGetMessage received) {
		Chunk chunk = World.INSTANCE.getChunk(received.attachment);
		connection.send(Messages.CHUNK_TERRAIN.build(chunk));
	}

}
