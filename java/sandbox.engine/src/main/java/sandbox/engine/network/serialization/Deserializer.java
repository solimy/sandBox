package sandbox.engine.network.serialization;

import java.nio.ByteBuffer;

public interface Deserializer {
	public Serializable decodePayload(ByteBuffer buffer);
}
