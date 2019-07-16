package sandbox.engine.network.serialization.nativ;

import java.nio.ByteBuffer;

import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public class SerializableInteger implements Serializable {
	public static final int TYPE = SerializableInteger.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		return new SerializableInteger(buffer.getInt());
	};

	public final Integer intValue;
	
	public SerializableInteger(Integer intValue) {
		this.intValue = intValue;
	}
	
	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer
				.allocate(Integer.BYTES)
				.putInt(intValue);
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
}
