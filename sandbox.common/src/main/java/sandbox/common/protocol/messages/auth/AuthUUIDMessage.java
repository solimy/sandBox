package sandbox.common.protocol.messages.auth;

import java.nio.ByteBuffer;
import java.util.UUID;

import sandbox.common.misc.serializer.UUIDSerializer;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class AuthUUIDMessage extends Message<AuthUUIDMessage, UUID> {

	protected AuthUUIDMessage(UUID uuid) {
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

	public static final Integer type = AuthUUIDMessage.class.getName().hashCode();
	public static final MessageAllocator<AuthUUIDMessage, UUID> allocator = (uuid) -> new AuthUUIDMessage(uuid);
}
