package sandbox.common.protocol.messages.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

import sandbox.common.misc.serializer.UUIDSerializer;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class EntityDeathMessage extends Message<EntityDeathMessage, UUID> {

	protected EntityDeathMessage(UUID uuid) {
		super(type, uuid);
	}

	@Override
	protected ByteBuffer encode() {
		ByteBuffer uuid = UUIDSerializer.INSTANCE.encode(attachment);
		uuid.flip();
		return ByteBuffer.allocate(uuid.remaining()).put(uuid);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		attachment = UUIDSerializer.INSTANCE.decode(attachment, inputBuffer);
	}

	public static Integer type = EntityDeathMessage.class.getName().hashCode();
	public static MessageAllocator<EntityDeathMessage, UUID> allocator = (uuid) -> new EntityDeathMessage(uuid);
}
