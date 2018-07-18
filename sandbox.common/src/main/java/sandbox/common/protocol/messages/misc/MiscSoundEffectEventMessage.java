package sandbox.common.protocol.messages.misc;

import java.nio.ByteBuffer;

import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class MiscSoundEffectEventMessage extends Message<MiscSoundEffectEventMessage, Integer> {

	protected MiscSoundEffectEventMessage(Integer effectId) {
		super(MiscSoundEffectEventMessage.type, effectId);
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

	public static final Integer type = MiscSoundEffectEventMessage.class.getName().hashCode();
	public static final MessageAllocator<MiscSoundEffectEventMessage, Integer> allocator = (effectId) -> new MiscSoundEffectEventMessage(effectId);
}
