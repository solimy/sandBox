package sandbox.server.network.message.handler.chunk;

import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainMessage;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum ChunkTerrainGetMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage rawMessage) {
		ChunkTerrainGetMessage received = new ChunkTerrainGetMessage(rawMessage);
		Chunk chunk = World.INSTANCE.getChunk(received.coordinates);
		connection.send(new ChunkTerrainMessage(chunk));
	}

}
