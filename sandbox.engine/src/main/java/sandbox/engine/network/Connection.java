package sandbox.engine.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Consumer;

import sandbox.engine.logging.Logger;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class Connection {

	private final AsynchronousSocketChannel socketChannel;
	private final ReadHandler readHandler;
	private final WriteHandler writeHandler;
	private final Consumer<Connection> onConnectionClosedHandler;
	public String remoteAdress;

	public Connection(AsynchronousSocketChannel socketChannel, Consumer<Connection> onConnectionClosedHandler) {
		this.socketChannel = socketChannel;
		this.onConnectionClosedHandler = onConnectionClosedHandler;
		readHandler = new ReadHandler(this);
		writeHandler = new WriteHandler(this);
	}

	public void listen() {
		String remoteAdress = "REMOTE ADDRESS EXCEPTION";
		try {
			remoteAdress = socketChannel.getRemoteAddress().toString();
		} catch (IOException e) {
			Logger.INSTANCE.error("Connection::Connection : " + e.toString() + " - " + e.getMessage());
			e.printStackTrace();
		} finally {
			this.remoteAdress = remoteAdress;
		}
		readHandler.read();
	}

	public void send(RawMessage message) {
		writeHandler.write(message);
	}

	public void send(ProtocolMessage message) {
		writeHandler.write(message.getRawMessage());
		Logger.INSTANCE.debug("Connection : message sent : {\"type: \"" + message.getRawMessage().getHeader().messageType + ", \"name\"\"" + message.getClass() + "\"}");
	}

	public boolean isOpen() {
		return socketChannel.isOpen();
	}

	void write(ByteBuffer peek) {
		socketChannel.write(peek, null, writeHandler);
	}

	public synchronized void close() {
		if (isOpen()) {
			try {
				socketChannel.close();
				onConnectionClosedHandler.accept(this);
			} catch (IOException e) {
				Logger.INSTANCE.error("Connection::close : " + e.toString() + " - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void read(ByteBuffer buffer) {
		socketChannel.read(buffer, null, readHandler);
	}
}
