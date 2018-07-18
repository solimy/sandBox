package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

import sandbox.common.world.enums.EntityNature;

public enum EntityNatureSerializer implements Serializer<EntityNature> {
	INSTANCE;

	@Override
	public ByteBuffer encode(EntityNature e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, Integer.BYTES);
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		return encoded.putInt(e.ordinal());
	}

	@Override
	public EntityNature decode(EntityNature e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		return e = EntityNature.getByOrdinal(buffer.getInt());
	}
}
