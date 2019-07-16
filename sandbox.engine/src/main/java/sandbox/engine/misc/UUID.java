package sandbox.engine.misc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import sandbox.engine.logging.Logger;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.RawMessage;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public class UUID implements Serializable {
	public static final Integer TYPE = UUID.class.getName().hashCode();

	public static final Deserializer DESERIALIZER = byteBuffer -> {
		long value = byteBuffer.getLong();
		return new UUID(value);
	};
	
	public final Long value;
	
	public UUID() {
		this((System.currentTimeMillis() << 20) | (System.nanoTime() & ~9223372036854251520L));
	}

	public UUID(long value) {
		this.value = value;
	}

	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer.allocate(Long.BYTES).putLong(value);
	}

	@Override
	public Integer getType() {
		return UUID.TYPE;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	
	public static void main(String[] args) {
		List<UUID> uuids = Arrays.asList(new UUID(), new UUID());
		uuids.forEach(Logger.INSTANCE::debug);
		RawMessage rawMessage = new RawMessage(0, uuids.toArray(new Serializable[uuids.size()]));
		ByteBuffer byteBuffer = rawMessage.getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer = byteBuffer.compact();
		rawMessage = new RawMessage(header, byteBuffer);
		for (int i = 0; i < uuids.size(); ++i) {
			Logger.INSTANCE.debug(((UUID) rawMessage.getWord(i)));
		}
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}
	
	public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        UUID other = (UUID) obj;
        return this.value.equals(other.value);
    }
}
