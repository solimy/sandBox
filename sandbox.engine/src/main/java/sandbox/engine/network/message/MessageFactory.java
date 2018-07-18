package sandbox.engine.network.message;

import java.nio.ByteBuffer;

public interface MessageFactory {
	@SuppressWarnings("serial")
	public static class UnknonwMessageTypeException extends Throwable {
		public UnknonwMessageTypeException(int type) {
			super(String.valueOf(type));
		}
	}

	Message<?, ?> build(Integer messageType, ByteBuffer inputBuffer) throws UnknonwMessageTypeException;
}
