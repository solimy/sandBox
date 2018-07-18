package sandbox.common.protocol.messages.test;

import java.nio.ByteBuffer;

import sandbox.common.protocol.Messages;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class TestPingMessage extends Message<TestPingMessage, Object> {

	protected TestPingMessage(Object notUsed) {
		super(TestPingMessage.type, notUsed);
	}

	@Override
	protected ByteBuffer encode() {
		return null;
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
	}
	
	public static final Integer type = TestPingMessage.class.getName().hashCode();
	public static final MessageAllocator<TestPingMessage, Object> allocator = (notUsed) -> new TestPingMessage(notUsed);
}
