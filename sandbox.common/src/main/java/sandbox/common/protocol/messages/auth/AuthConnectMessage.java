package sandbox.common.protocol.messages.auth;

import java.nio.ByteBuffer;

import sandbox.engine.logging.Logger;
import sandbox.engine.misc.Token;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class AuthConnectMessage extends ProtocolMessage {
	public static final Integer TYPE = AuthConnectMessage.class.getName().hashCode();

	public final Token token;

	public AuthConnectMessage(Token token) {
		super(new RawMessage(TYPE, token));
		this.token = token;
	}


	public AuthConnectMessage(RawMessage rawMessage) {
		super(rawMessage);
		token = (Token) rawMessage.getWord(0);
	}

	public static void main(String[] args) {
		AuthConnectMessage authConnectMessage = new AuthConnectMessage(new Token());
		Logger.INSTANCE.debug(authConnectMessage.token);
		ByteBuffer byteBuffer = authConnectMessage.getRawMessage().getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer.compact();
		authConnectMessage = new AuthConnectMessage(new RawMessage(header, byteBuffer));
		Logger.INSTANCE.debug(authConnectMessage.token);
	}
}
