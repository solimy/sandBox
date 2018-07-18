package sandbox.common.protocol.messages.chunk;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.common.misc.serializer.CoordinatesSerializer;
import sandbox.common.world.model.Chunk;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class ChunkEntitiesGetMessage extends Message<ChunkEntitiesGetMessage, Coordinates> {

	protected ChunkEntitiesGetMessage(Coordinates coordinates) {
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

	public static final Integer type = ChunkEntitiesGetMessage.class.getName().hashCode();
	public static final MessageAllocator<ChunkEntitiesGetMessage, Coordinates> allocator = (coordinates) -> new ChunkEntitiesGetMessage(coordinates);
}
