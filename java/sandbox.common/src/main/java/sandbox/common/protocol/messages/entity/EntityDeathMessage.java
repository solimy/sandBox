package sandbox.common.protocol.messages.entity;

import sandbox.engine.game.Event;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class EntityDeathMessage extends ProtocolMessage implements Event {
	public static Integer TYPE = EntityDeathMessage.class.getName().hashCode();

	public final UUID uuid;
	
	public EntityDeathMessage(UUID uuid) {
		super(new RawMessage(TYPE, uuid));
		this.uuid = uuid;
	}

	public EntityDeathMessage(RawMessage rawMessage) {
		super(rawMessage);
		uuid = (UUID) rawMessage.getWord(0);
	}
}
