package sandbox.engine.network;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

import sandbox.engine.logging.Logger;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private final AsyncNIOServer server;
	private final Consumer<Connection> onConnectionHandler;
	private final Consumer<Connection> onDisconnectionHandler;

	public ConnectionHandler(
			AsyncNIOServer server,
			Consumer<Connection> onConnectionHandler,
			Consumer<Connection> onDisconnectionHandler) {
		this.server = server;
		this.onConnectionHandler = onConnectionHandler;
		this.onDisconnectionHandler = onDisconnectionHandler;
	}

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, Void unsused) {
		this.server.getSocketChannel().accept(null, this);
		Connection connection = new Connection(socketChannel, onDisconnectionHandler);
		onConnectionHandler.accept(connection);
		connection.listen();
	}

	@Override
	public void failed(Throwable exc, Void unused) {
		Logger.INSTANCE.error("ConnectionHandler::failed");
		exc.printStackTrace();
	}
}
