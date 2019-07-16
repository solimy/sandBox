package sandbox.engine.network;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import sandbox.engine.Engine;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public class ReadHandler implements CompletionHandler<Integer, Void> {
	private final Connection connection;
	private Header header = null;
	public final ByteBuffer buffer = ByteBuffer.allocate(RawMessage.MAX_SIZE);

	public ReadHandler(
			Connection connection) {
		this.connection = connection;
		buffer.limit(Header.SIZE);
	}

	@Override
	public void completed(Integer result, Void nothingAttached) {
		if (result < 0) {
			connection.close();
			return;
		}
		if (buffer.remaining() == 0) {
			if (header == null) {
				buffer.flip();
				header = new Header(buffer);
				buffer.clear();
				buffer.limit(header.messageSize);
			} else {
				buffer.flip();
				RawMessage rawMessage = new RawMessage(header, buffer);
				// TODO : maybe use thread pool for message handling
				MessageHandlingService.INSTANCE.handle(connection, rawMessage);
				buffer.clear();
				buffer.limit(Header.SIZE);
				header = null;
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
