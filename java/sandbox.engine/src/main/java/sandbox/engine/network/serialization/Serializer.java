package sandbox.engine.network.serialization;

import java.nio.ByteBuffer;

public final class Serializer {
	
	public static ByteBuffer serialize(Serializable serializable) {
		ByteBuffer encoded = serializable.encodePayload();
		encoded.rewind();
		ByteBuffer fullyEncoded = ByteBuffer.allocate(Integer.BYTES * 2 + encoded.capacity());
		fullyEncoded.putInt(serializable.getType());
		fullyEncoded.putInt(encoded.capacity());
		fullyEncoded.put(encoded);
		fullyEncoded.rewind();
		return fullyEncoded;
	}
	

	public static Serializable deserializeNext(ByteBuffer buffer) {
		Serializable serializable = null;
		int type = buffer.getInt();
		int size = buffer.getInt();
		if (size <= 0) {
			return null;
		}
		Deserializer deserializer = SerializableRegistryService.INSTANCE.get(type);
		serializable = deserializer.decodePayload(buffer);
		return serializable;
	}
}
