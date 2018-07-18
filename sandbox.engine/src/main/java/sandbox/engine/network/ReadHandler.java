package sandbox.engine.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import sandbox.engine.misc.unsafe.ThrowingRunnable;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageFactory.UnknonwMessageTypeException;

public class ReadHandler implements CompletionHandler<Integer, Void> {

	private final Connection connection;
	public final ByteBuffer buffer = ByteBuffer.allocate(Message.MAX_SIZE);
	private final Header header = new Header();

	public ReadHandler(Connection connection) {
		this.connection = connection;
		buffer.limit(Header.getSize());
	}

	@Override
	public void completed(Integer result, Void nothingAttached) {
		if (result < 0) {
			connection.close();
			return;
		}
		if (buffer.remaining() == 0) {
			if (header.getMessageSize() == null) {
				buffer.flip();
				header.read(buffer);
				buffer.clear();
				buffer.limit(header.getMessageSize());
			} else {
				try {
					buffer.flip();
					Message<?, ?> message = connection.getMessageFactory().build(header.getMessageType(), buffer);
					buffer.clear();
					buffer.limit(Header.getSize());
					header.reset();
					connection.getMessageHandler().handle(connection, message);
				} catch (UnknonwMessageTypeException e) {
					e.printStackTrace();
				}
			}
		}
		read();
	}

	public void read() {
		connection.read(buffer);
	}

	@Override
	public void failed(Throwable exc, Void nothingAttached) {
		connection.close();
	}
}
