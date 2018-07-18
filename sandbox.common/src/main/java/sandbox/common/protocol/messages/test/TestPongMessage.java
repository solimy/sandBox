package sandbox.common.protocol.messages.test;

import java.nio.ByteBuffer;

import sandbox.common.protocol.Messages;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class TestPongMessage extends Message<TestPongMessage, Object> {

	protected TestPongMessage(Object notUsed) {
		super(TestPongMessage.type, notUsed);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ByteBuffer encode() {
		return null;
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
	}

	public static final Integer type = TestPongMessage.class.getName().hashCode();
	public static final MessageAllocator<TestPongMessage, Object> allocator = (notUsed) -> new TestPongMessage(notUsed);
}
