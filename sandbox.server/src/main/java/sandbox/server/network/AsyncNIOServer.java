package sandbox.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;

import sandbox.common.protocol.MessageFactoryImpl;
import sandbox.server.game.GameServer;
import sandbox.server.network.message.handler.ServerMessageHandler;

public class AsyncNIOServer {
	private final String address;
	private final int port;
	private boolean alive;
	private AsynchronousServerSocketChannel socketChannel;

	public AsyncNIOServer(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public void start() throws IOException {
		this.socketChannel = AsynchronousServerSocketChannel.open();
		this.socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		this.socketChannel.bind(new InetSocketAddress(this.address, this.port));
		this.alive = true;
		this.socketChannel.accept(null,
				new ConnectionHandler(this, new MessageFactoryImpl(), new ServerMessageHandler()));
		System.out.println("AsyncNIOServer::start Server listening on port " + port);
	}

	public boolean isAlive() {
		return alive;
	}

	public void kill() {
		alive = false;
	}

	public AsynchronousServerSocketChannel getSocketChannel() {
		return socketChannel;
	}
}