package sandbox.common.protocol.messages.entity;

import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class EntityUpdateGetMessage extends ProtocolMessage {
	public static final Integer TYPE = EntityUpdateGetMessage.class.getName().hashCode();

	public final UUID uuid;
	
	public EntityUpdateGetMessage(UUID uuid) {
		super(new RawMessage(TYPE, uuid));
		this.uuid = uuid;
	}
	
	public EntityUpdateGetMessage(RawMessage rawMessage) {
		super(rawMessage);
		uuid = (UUID) rawMessage.getWord(0);
	}
}
