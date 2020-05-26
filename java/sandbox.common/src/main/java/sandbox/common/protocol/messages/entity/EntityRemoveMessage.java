package sandbox.common.protocol.messages.entity;

import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class EntityRemoveMessage extends ProtocolMessage {
	public static Integer TYPE = EntityRemoveMessage.class.getName().hashCode();

	public final UUID uuid;
	
	public EntityRemoveMessage(UUID uuid) {
		super(new RawMessage(TYPE, uuid));
		this.uuid = uuid;
	}
	
	public EntityRemoveMessage(RawMessage rawMessage) {
		super(rawMessage);
		uuid = (UUID) rawMessage.getWord(0);
	}
}
