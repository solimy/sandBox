package sandbox.engine.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class AsyncNIOClient {
	private final AsynchronousSocketChannel socketChannel;
	private final Consumer<Connection> onConnectionHandler;
	private final Consumer<Connection> onDisconnectionHandler;
	private Connection connection;
	
	public AsyncNIOClient(Consumer<Connection> onConnectionHandler, Consumer<Connection> onDisconnectionHandler) throws IOException {
		this.onConnectionHandler = onConnectionHandler;
		this.onDisconnectionHandler = onDisconnectionHandler;
		this.socketChannel = AsynchronousSocketChannel.open();
	}

	public void connect(String ip, Integer port) {
		connection = new Connection(socketChannel, onDisconnectionHandler);
		Future<Void> connect = socketChannel.connect(new InetSocketAddress(ip, port));
		try {
			connect.get();
			onConnectionHandler.accept(connection);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		connection.listen();
	}
}
