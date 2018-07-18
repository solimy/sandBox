package sandbox.common.protocol.messages.chunk;

import java.nio.ByteBuffer;

import sandbox.common.misc.serializer.ChunkTerrainSerializer;
import sandbox.common.world.model.Chunk;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class ChunkTerrainMessage extends Message<ChunkTerrainMessage, Chunk> {

	protected ChunkTerrainMessage(Chunk chunk) {
		super(type, chunk);
	}

	@Override
	protected ByteBuffer encode() {
		return ChunkTerrainSerializer.INSTAnCE.encode(attachment);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		attachment = ChunkTerrainSerializer.INSTAnCE.decode(attachment, inputBuffer);
	}

	public static final Integer type = ChunkTerrainMessage.class.getName().hashCode();
	public static final MessageAllocator<ChunkTerrainMessage, Chunk> allocator = (chunk) -> new ChunkTerrainMessage(chunk);
}
