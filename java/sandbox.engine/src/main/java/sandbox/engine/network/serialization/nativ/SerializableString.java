package sandbox.engine.network.serialization.nativ;

import java.nio.ByteBuffer;

import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public class SerializableString implements Serializable {
	public static final int TYPE = SerializableString.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		int len = buffer.getInt();
		byte bytes[] = new byte[len];
		buffer.get(bytes);
		String stringValue = new String(bytes);
		return new SerializableString(stringValue);
	};

	public final String stringValue;
	
	public SerializableString(String stringValue) {
		this.stringValue = stringValue;
	}
	
	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer
				.allocate(Integer.BYTES + Byte.BYTES*stringValue.length())
				.putInt(stringValue.length())
				.put(stringValue.getBytes());
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
}
