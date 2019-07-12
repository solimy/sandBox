package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;
import java.util.UUID;

public enum UUIDSerializer implements Serializer<UUID> {
	INSTANCE;

	@Override
	public ByteBuffer encode(UUID e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, Integer.BYTES + (e != null ? e.toString().length() : 0));
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		byte[] uuid = e.toString().getBytes();
		//System.out.println("encoding uuid (capacity=" + encoded.capacity() + ") : \"" + e.toString() + "\"");
		return encoded.putInt(uuid.length).put(uuid);
	}

	@Override
	public UUID decode(UUID e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		byte[] uuid = new byte[buffer.getInt()];
		buffer.get(uuid);
		//System.out.println("decoding uuid (length=" + uuid.length + ") : \"" + new String(uuid) + "\"");
		e = UUID.fromString(new String(uuid));
		return e;
	}
}
