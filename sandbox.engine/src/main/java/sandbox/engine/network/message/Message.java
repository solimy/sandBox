package sandbox.engine.network.message;

import java.nio.ByteBuffer;

import sandbox.engine.game.Event;

public abstract class Message<E extends Message<E, A>, A> implements Event {
	public static Integer MAX_SIZE = 4096;
	public A attachment;
	public final Integer type;
	
	protected Message(Integer type, A attachment) {
		this.attachment = attachment;
		this.type = type;
	}

	public ByteBuffer getBuffer() {
		ByteBuffer outputBuffer = null;
		ByteBuffer messageBuffer = encode();
		if (messageBuffer == null) {
			outputBuffer = ByteBuffer.allocate(Header.getSize());
			outputBuffer.putInt(0);
			outputBuffer.putInt(type);
			outputBuffer.flip();
			return outputBuffer;
		}
		if (messageBuffer.remaining() == 0)
			messageBuffer.flip();
		outputBuffer = ByteBuffer.allocate(Header.getSize() + messageBuffer.remaining());
		outputBuffer.putInt(messageBuffer.remaining());
		outputBuffer.putInt(type);
		outputBuffer.put(messageBuffer);
		outputBuffer.flip();
		return outputBuffer;
	}

	public void read(ByteBuffer inputBuffer) {
		decode(inputBuffer);
	}

	protected abstract ByteBuffer encode();

	protected abstract void decode(ByteBuffer inputBuffer);
}
