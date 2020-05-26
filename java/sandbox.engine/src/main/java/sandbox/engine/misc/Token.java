package sandbox.engine.misc;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sandbox.engine.logging.Logger;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.RawMessage;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public class Token implements Serializable {
	public static final Integer TYPE = Token.class.getName().hashCode();
	public static final Integer LENGTH = 20;

	public static final Deserializer DESERIALIZER = byteBuffer -> {
		byte[] token = new byte[20];
		byteBuffer.get(token);
		return new Token(token);
	};

	private final byte[] token;
	
	public Token(byte[] token) {
		this.token = token;
	}
	
	public Token() {
		token = new byte[LENGTH];
		new SecureRandom().nextBytes(token);
	}

	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer.allocate(20).put(token);
	}

	@Override
	public Integer getType() {
		return Token.TYPE;
	}
	
	@Override
	public String toString() {
		return new String(token);
	}
	
	public static void main(String[] args) {
		List<Token> tokens = Arrays.asList(new Token(), new Token(), new Token(), new Token(), new Token(), new Token());
		tokens.forEach(Logger.INSTANCE::debug);
		RawMessage rawMessage = new RawMessage(0, tokens.toArray(new Serializable[tokens.size()]));
		ByteBuffer byteBuffer = rawMessage.getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer = byteBuffer.compact();
		rawMessage = new RawMessage(header, byteBuffer);
		for (int i = 0; i < tokens.size(); ++i) {
			Logger.INSTANCE.debug(((Token) rawMessage.getWord(i)));
		}
	}
}
