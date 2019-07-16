package sandbox.common.protocol.messages.entity;

import sandbox.common.game.events.Damage;
import sandbox.engine.game.Event;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;


public class EntityDamagedMessage extends ProtocolMessage implements Event {
	public static Integer TYPE = EntityDamagedMessage.class.getName().hashCode();

	public final UUID uuid;
	public final Damage damage;
	
	public EntityDamagedMessage(UUID uuid, Damage damage) {
		super(new RawMessage(TYPE, uuid, damage));
		this.uuid = uuid;
		this.damage = damage;
	}

	public EntityDamagedMessage(RawMessage rawMessage) {
		super(rawMessage);
		uuid = (UUID) rawMessage.getWord(0);
		damage = (Damage) rawMessage.getWord(1);
	}
}
