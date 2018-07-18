package sandbox.common.protocol.messages.entity;

import java.nio.ByteBuffer;

import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class EntityUseActionMessage extends Message<EntityUseActionMessage, Object> {

	protected EntityUseActionMessage(Object notUsed) {
		super(EntityUseActionMessage.type, notUsed);
	}

	@Override
	protected ByteBuffer encode() {
		return null;
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
	}

	public static Integer type = EntityUseActionMessage.class.getName().hashCode();
	public static MessageAllocator<EntityUseActionMessage, Object> allocator = (notUsed) -> new EntityUseActionMessage(notUsed);
}
