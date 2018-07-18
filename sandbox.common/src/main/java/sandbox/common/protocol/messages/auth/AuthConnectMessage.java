package sandbox.common.protocol.messages.auth;

import java.nio.ByteBuffer;

import sandbox.common.misc.Token;
import sandbox.common.misc.serializer.TokenSerializer;
import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.test.TestPingMessage;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class AuthConnectMessage extends Message<AuthConnectMessage, Token> {

	protected AuthConnectMessage(Token token) {
		super(type, token);
	}

	@Override
	protected ByteBuffer encode() {
		return TokenSerializer.INSTANCE.encode(attachment);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		TokenSerializer.INSTANCE.decode(attachment, inputBuffer);
	}

	public static final Integer type = AuthConnectMessage.class.getName().hashCode();
	public static final MessageAllocator<AuthConnectMessage, Token> allocator = (token) -> new AuthConnectMessage(token);
}
