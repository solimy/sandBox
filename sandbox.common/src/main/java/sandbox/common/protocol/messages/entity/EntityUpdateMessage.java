package sandbox.common.protocol.messages.entity;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;
import sandbox.engine.network.serialization.nativ.SerializableString;

public class EntityUpdateMessage extends ProtocolMessage {
	public static final Integer TYPE = EntityUpdateMessage.class.getName().hashCode();

	public final UUID uuid;
	public final WorldEntityComponent worldEntityComponent;
	public final String entityName;
	
	public EntityUpdateMessage(UUID uuid, WorldEntityComponent worldEntityComponent, String entityName) {
		super(new RawMessage(TYPE, uuid, worldEntityComponent, new SerializableString(entityName)));
		this.uuid = uuid;
		this.worldEntityComponent = worldEntityComponent;
		this.entityName = entityName;
	}

	public EntityUpdateMessage(RawMessage rawMessage) {
		super(rawMessage);
		uuid = (UUID) rawMessage.getWord(0);
		worldEntityComponent = (WorldEntityComponent) rawMessage.getWord(1);
		entityName = ((SerializableString)rawMessage.getWord(2)).stringValue;
	}
}
