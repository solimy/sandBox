package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

public interface Serializer<E> {
	ByteBuffer encode(E e);

	E decode(E e, ByteBuffer buffer);

	default ByteBuffer allocateAndSetApplicability(E e, Integer capacity) {
		ByteBuffer buffer;
		if (e == null) {
			buffer = ByteBuffer.allocate(Byte.BYTES);
			buffer.put(Byte.MIN_VALUE);
			return buffer;
		}
		buffer = ByteBuffer.allocate(Byte.BYTES + capacity);
		buffer.put(Byte.MAX_VALUE);
		return buffer;
	}
	
	default Boolean isApplicable(ByteBuffer buffer) {
		return buffer.get() == Byte.MAX_VALUE;
	}
}
