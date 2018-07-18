package sandbox.common.protocol.messages.chunk;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.common.misc.serializer.CoordinatesSerializer;
import sandbox.common.world.model.Chunk;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class ChunkTerrainGetMessage extends Message<ChunkTerrainGetMessage, Coordinates> {

	protected ChunkTerrainGetMessage(Coordinates coordinates) {
		super(type, coordinates);
	}

	@Override
	protected ByteBuffer encode() {
		return CoordinatesSerializer.INSTANCE.encode(attachment);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		attachment = CoordinatesSerializer.INSTANCE.decode(attachment, inputBuffer);
	}

	public static final Integer type = ChunkTerrainGetMessage.class.getName().hashCode();
	public static final MessageAllocator<ChunkTerrainGetMessage, Coordinates> allocator = (coordinates) -> new ChunkTerrainGetMessage(coordinates);
}
