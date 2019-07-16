package sandbox.engine.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.function.Consumer;

import sandbox.engine.logging.Logger;

public class AsyncNIOServer {
	private final String address;
	private final int port;
	private final Consumer<Connection> onConnectionHandler;
	private final Consumer<Connection> onDisconnectionHandler;
	private boolean alive;
	private AsynchronousServerSocketChannel socketChannel;

	public AsyncNIOServer(
			String address,
			int port,
			Consumer<Connection> onConnectionHandler,
			Consumer<Connection> onDisconnectionHandler) {
		this.address = address;
		this.port = port;
		this.onConnectionHandler = onConnectionHandler;
		this.onDisconnectionHandler = onDisconnectionHandler;
	}

	public void start() throws IOException {
		this.socketChannel = AsynchronousServerSocketChannel.open();
		this.socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.socketChannel.bind(new InetSocketAddress(this.address, this.port));
		this.alive = true;
		this.socketChannel.accept(null,
				new ConnectionHandler(
						this,
						onConnectionHandler,
						onDisconnectionHandler));
		Logger.INSTANCE.debug("AsyncNIOServer::start Server listening on port " + port);
	}

	public boolean isAlive() {
		return alive;
	}

	public void stop() {
		alive = false;
		throw new Error("AsyncNIOServer: feature not implemented\".stop()\"");
	}

	public AsynchronousServerSocketChannel getSocketChannel() {
		return socketChannel;
	}
}