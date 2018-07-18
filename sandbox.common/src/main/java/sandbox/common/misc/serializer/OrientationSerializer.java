package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

import sandbox.engine.math.CardinalOrientation;

public enum OrientationSerializer implements Serializer<CardinalOrientation> {
	INSTANCE;

	@Override
	public ByteBuffer encode(CardinalOrientation e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, Integer.BYTES);
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		return encoded.putInt(e.ordinal());
	}

	@Override
	public CardinalOrientation decode(CardinalOrientation e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		return e = CardinalOrientation.getByOrdinal(buffer.getInt());
	}
}
