package sandbox.common.protocol.messages.misc;

import java.nio.ByteBuffer;

import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class MiscVisualEffectEventMessage extends Message<MiscVisualEffectEventMessage, Integer> {

	protected MiscVisualEffectEventMessage(Integer effectId) {
		super(MiscVisualEffectEventMessage.type, effectId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		// TODO Auto-generated method stub
		
	}

	public static final Integer type = MiscVisualEffectEventMessage.class.getName().hashCode();
	public static final MessageAllocator<MiscVisualEffectEventMessage, Integer> allocator = (effectId) -> new MiscVisualEffectEventMessage(effectId);
}
