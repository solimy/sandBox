package sandbox.common.protocol;

import java.nio.ByteBuffer;

import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageFactory;

public class MessageFactoryImpl implements MessageFactory {
	@Override
	public Message<?, ?> build(Integer messageType, ByteBuffer inputBuffer) throws UnknonwMessageTypeException {
		Message<?, ?> message = Messages.getByMessageCode(messageType).build(null);
		message.read(inputBuffer);
		return message;
	}
}
