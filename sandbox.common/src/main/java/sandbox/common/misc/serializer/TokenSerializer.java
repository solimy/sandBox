package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

import sandbox.common.misc.Token;

public enum TokenSerializer implements Serializer<Token> {
	INSTANCE;

	@Override
	public ByteBuffer encode(Token e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, Token.LENGTH);
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		return encoded.put(e.token);
	}

	@Override
	public Token decode(Token e, ByteBuffer buffer) {
		if (e == null)
			e = new Token();
		if (!isApplicable(buffer))
			return e;
		buffer.get(e.token);
		return e;
	}

}
