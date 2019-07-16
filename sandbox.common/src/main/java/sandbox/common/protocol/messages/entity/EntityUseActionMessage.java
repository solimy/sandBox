package sandbox.common.protocol.messages.entity;

import sandbox.engine.game.Event;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;
import sandbox.engine.network.serialization.Serializable;

public class EntityUseActionMessage extends ProtocolMessage implements Event {
	public static final int TYPE = EntityUseActionMessage.class.getName().hashCode();

	public EntityUseActionMessage() {
		super(new RawMessage(TYPE, (Serializable[])null));
	}
	
	public EntityUseActionMessage(RawMessage rawMessage) {
		super(rawMessage);
	}
}
