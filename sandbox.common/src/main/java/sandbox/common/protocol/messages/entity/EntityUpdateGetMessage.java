package sandbox.common.protocol.messages.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

import sandbox.common.misc.serializer.UUIDSerializer;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class EntityUpdateGetMessage extends Message<EntityUpdateGetMessage, UUID> {

	protected EntityUpdateGetMessage(UUID uuid) {
		super(type, uuid);
	}

	@Override
	protected ByteBuffer encode() {
		return UUIDSerializer.INSTANCE.encode(attachment);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		attachment = UUIDSerializer.INSTANCE.decode(attachment, inputBuffer);
	}

	public static final Integer type = EntityUpdateGetMessage.class.getName().hashCode();
	public static final MessageAllocator<EntityUpdateGetMessage, UUID> allocator = (uuid) -> new EntityUpdateGetMessage(uuid);
}
