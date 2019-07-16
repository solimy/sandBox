package sandbox.client.network.message.handler.chunk;

import sandbox.client.ClientScript;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainMessage;
import sandbox.common.world.model.World;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum ChunkTerrainMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		ChunkTerrainMessage terrain = new ChunkTerrainMessage(message);
		World.INSTANCE.putChunkTerrain(terrain.chunk.coordinates, terrain.chunk);
		ClientScript.INSTANCE.connection.send(new ChunkEntitiesGetMessage(terrain.chunk.coordinates));
	}
}
