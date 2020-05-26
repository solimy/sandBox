package sandbox.engine.network.serialization;

import java.nio.ByteBuffer;

public interface Serializable {
	public ByteBuffer encodePayload();
	public Integer getType();
}
