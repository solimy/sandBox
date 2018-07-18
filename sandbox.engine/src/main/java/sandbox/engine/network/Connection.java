package sandbox.engine.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageFactory;
import sandbox.engine.network.message.handler.MessageHandler;

public class Connection {

	private final AsynchronousSocketChannel socketChannel;
	private final ReadHandler readHandler;
	private final WriteHandler writeHandler;
	private final MessageFactory messageFactory;
	private final MessageHandler<Message<?, ?>> messageHandler;
	private final Runnable onConnectionClosedHandler;
	public String remoteAdress;

	public Connection(AsynchronousSocketChannel socketChannel, MessageFactory messageFactory,
			MessageHandler<Message<?, ?>> messageHandler, Runnable onConnectionClosedHandler) {
		this.socketChannel = socketChannel;
		this.messageFactory = messageFactory;
		this.messageHandler = messageHandler;
		this.onConnectionClosedHandler = onConnectionClosedHandler;
		readHandler = new ReadHandler(this);
		writeHandler = new WriteHandler(this);
	}

	public void listen() {
		String remoteAdress = "REMOTE ADDRESS EXCEPTION";
		try {
			remoteAdress = socketChannel.getRemoteAddress().toString();
		} catch (IOException e) {
			System.err.println("Connection::Connection : " + e.toString() + " - " + e.getMessage());
			e.printStackTrace();
		} finally {
			this.remoteAdress = remoteAdress;
		}
		readHandler.read();
	}

	public void send(Message<?, ?> message) {
		writeHandler.write(message);
	}

	public boolean isOpen() {
		return socketChannel.isOpen();
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public MessageHandler<Message<?, ?>> getMessageHandler() {
		return messageHandler;
	}

	void write(ByteBuffer peek) {
		socketChannel.write(peek, null, writeHandler);
	}

	public synchronized void close() {
		if (isOpen()) {
			try {
				socketChannel.close();
				onConnectionClosedHandler.run();
			} catch (IOException e) {
				System.err.println("Connection::close : " + e.toString() + " - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void read(ByteBuffer buffer) {
		socketChannel.read(buffer, null, readHandler);
	}
}
