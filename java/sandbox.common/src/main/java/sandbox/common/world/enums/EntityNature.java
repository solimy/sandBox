package sandbox.common.world.enums;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public enum EntityNature implements Serializable {
	ENVIRONMENTAL, FIREBALL, PLAYER;
	
	public static final int TYPE = EntityNature.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		return getByOrdinal(buffer.getInt());
	};
	
	public static EntityNature getByOrdinal(Integer ordinal) {
		return entityNatures.get(ordinal);
	}
	
	private static final Map<Integer, EntityNature> entityNatures = new HashMap<>();
	static {
		for (int i = 0; i < EntityNature.values().length; i++) {
			entityNatures.put(EntityNature.values()[i].ordinal(), EntityNature.values()[i]);
		}
	}

	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer.allocate(Integer.BYTES).putInt(ordinal());
	}
	@Override
	public Integer getType() {
		return TYPE;
	}
}
