package sandbox.common.protocol.messages.combat;

import java.nio.ByteBuffer;

import sandbox.common.game.events.Damage;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class DamageNotificationMessage extends Message<DamageNotificationMessage, Damage> {

	protected DamageNotificationMessage(Damage damage) {
		super(type, damage);
	}

	@Override
	protected ByteBuffer encode() {
		return null;
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
	}


	public static final Integer type = DamageNotificationMessage.class.getName().hashCode();
	public static final MessageAllocator<DamageNotificationMessage, Damage> allocator = (damage) -> new DamageNotificationMessage(damage);
}
