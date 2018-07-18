package sandbox.engine.network;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import sandbox.engine.network.message.Message;

public class WriteHandler implements CompletionHandler<Integer, Void> {

	private final Connection connection;
	private final AtomicBoolean writing = new AtomicBoolean(false);
	Queue<ByteBuffer> messagesToSend = new ConcurrentLinkedQueue<>();

	public WriteHandler(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void completed(Integer result, Void nothingAttached) {
		if (result < 0) {
			connection.close();
			return;
		}
		synchronized (messagesToSend) {
			if (!messagesToSend.isEmpty() && messagesToSend.peek().remaining() == 0)
				messagesToSend.poll();
		}
		writing.set(false);
		write();
	}

	@Override
	public void failed(Throwable exc, Void nothingAttached) {
		connection.close();
	}

	public void write(Message<?, ?> message) {
		ByteBuffer buffer = message.getBuffer();
		messagesToSend.add(buffer);
		write();
	}

	private void write() {
		if (!messagesToSend.isEmpty() && !writing.getAndSet(true))
			connection.write(messagesToSend.peek());
	}
}