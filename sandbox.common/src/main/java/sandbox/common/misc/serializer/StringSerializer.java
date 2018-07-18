package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

public enum StringSerializer implements Serializer<String> {
	INSTANCE;

	@Override
	public ByteBuffer encode(String e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, Integer.BYTES + (e != null ? e.length() : 0));
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		byte[] string = e.getBytes();
		return encoded.putInt(string.length).put(string);
	}

	@Override
	public String decode(String e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		byte[] string = new byte[buffer.getInt()];
		buffer.get(string);
		e = new String(string);
		return e;
	}
}
