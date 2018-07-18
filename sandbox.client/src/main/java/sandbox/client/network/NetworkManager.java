package sandbox.client.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

import sandbox.client.ClientScript;
import sandbox.client.network.message.handler.ClientMessageHandler;
import sandbox.common.protocol.MessageFactoryImpl;
import sandbox.engine.network.Connection;

public class NetworkManager {
	private AsynchronousSocketChannel socketChannel;
	public Connection connection;
	
	public NetworkManager() {
		try {
			socketChannel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String ip, Integer port) {
		connection = new Connection(socketChannel, new MessageFactoryImpl(), new ClientMessageHandler(), ()->{
			System.out.println("Disconnected from server");
		});
		try {
			socketChannel.connect(new InetSocketAddress(ip, port)).get();
			connection.listen();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
