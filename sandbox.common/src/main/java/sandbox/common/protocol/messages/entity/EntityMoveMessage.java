package sandbox.common.protocol.messages.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

import sandbox.common.game.events.Move;
import sandbox.common.misc.serializer.OrientationSerializer;
import sandbox.common.misc.serializer.UUIDSerializer;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class EntityMoveMessage extends Message<EntityMoveMessage, Move> {

	protected EntityMoveMessage(Move move) {
		super(type, move);
	}

	@Override
	protected ByteBuffer encode() {
		ByteBuffer uuid = UUIDSerializer.INSTANCE.encode(attachment.getMovingUUID());
		uuid.flip();
		ByteBuffer direction = OrientationSerializer.INSTANCE.encode(attachment.getCardinalOrientation());
		direction.flip();
		return ByteBuffer.allocate(uuid.remaining() + direction.remaining()).put(uuid).put(direction);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		UUID uuid = UUIDSerializer.INSTANCE.decode(null, inputBuffer);
		CardinalOrientation orientation = OrientationSerializer.INSTANCE.decode(null, inputBuffer);
		attachment = new Move(uuid, orientation);
	}

	public static Integer type = EntityMoveMessage.class.getName().hashCode();
	public static MessageAllocator<EntityMoveMessage, Move> allocator = (move) -> new EntityMoveMessage(move);
}
