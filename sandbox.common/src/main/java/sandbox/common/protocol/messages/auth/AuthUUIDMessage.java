package sandbox.common.protocol.messages.auth;

import java.nio.ByteBuffer;

import sandbox.engine.logging.Logger;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class AuthUUIDMessage extends ProtocolMessage {
	public static final Integer TYPE = AuthUUIDMessage.class.getName().hashCode();

	public final UUID uuid;
	
	public AuthUUIDMessage(UUID uuid) {
		super(new RawMessage(TYPE, uuid));
		this.uuid = uuid;
	}
	
	public AuthUUIDMessage(RawMessage rawMessage) {
		super(rawMessage);
		this.uuid = (UUID) rawMessage.getWord(0);
	}

	public static void main(String[] args) {
		AuthUUIDMessage authUUIDMessage = new AuthUUIDMessage(new UUID());
		Logger.INSTANCE.debug(authUUIDMessage.uuid);
		ByteBuffer byteBuffer = authUUIDMessage.getRawMessage().getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer.compact();
		authUUIDMessage = new AuthUUIDMessage(new RawMessage(header, byteBuffer));
		Logger.INSTANCE.debug(authUUIDMessage.uuid);
	}
}
