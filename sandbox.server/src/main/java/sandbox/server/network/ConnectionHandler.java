package sandbox.server.network;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.UUID;

import sandbox.engine.game.Entity;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageFactory;
import sandbox.engine.network.message.handler.MessageHandler;
import sandbox.server.game.GameServer;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

	private final AsyncNIOServer server;
	private final MessageFactory messageFactory;
	private final MessageHandler<Message<?, ?>> messageHandler;

	public ConnectionHandler(AsyncNIOServer server, MessageFactory messageFactory, MessageHandler<Message<?, ?>> messageHandler) {
		this.server = server;
		this.messageFactory = messageFactory;
		this.messageHandler = messageHandler;
	}

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, Object nothingAttached) {
		this.server.getSocketChannel().accept(null, this);
		Entity player = new Entity(UUID.randomUUID());
		Connection connection = new Connection(socketChannel, messageFactory, messageHandler, player::remove);
		GameServer.INSTANCE.newPlayer(connection, player);
		
		connection.listen();
	}

	@Override
	public void failed(Throwable exc, Object nothingAttached) {
		System.err.println("ConnectionHandler::failed");
		exc.printStackTrace();
	}
}
