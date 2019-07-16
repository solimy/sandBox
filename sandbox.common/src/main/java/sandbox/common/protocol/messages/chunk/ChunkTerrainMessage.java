package sandbox.common.protocol.messages.chunk;

import sandbox.common.world.model.Chunk;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class ChunkTerrainMessage extends ProtocolMessage {
	public static final Integer TYPE = ChunkTerrainMessage.class.getName().hashCode();
	public final Chunk chunk;
	
	public ChunkTerrainMessage(Chunk chunk) {
		super(new RawMessage(TYPE, chunk));
		this.chunk = chunk;
	}

	
	public ChunkTerrainMessage(RawMessage rawMessage) {
		super(rawMessage);
		this.chunk = (Chunk) rawMessage.getWord(0);
	}
}
