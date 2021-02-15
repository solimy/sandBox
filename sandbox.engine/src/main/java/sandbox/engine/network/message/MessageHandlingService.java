package sandbox.engine.network.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sandbox.engine.Engine;
import sandbox.engine.logging.Logger;
import sandbox.engine.network.Connection;

public enum MessageHandlingService {
	INSTANCE;

	private final Map<Integer, MessageHandler> messageHandlers = new ConcurrentHashMap<Integer, MessageHandler>();
	
	public void register(int type, MessageHandler handler) {
		messageHandlers.put(type, handler);
	}
	
	public void handle(Connection connection, RawMessage rawMessage) {
		Integer type = rawMessage.getHeader().messageType;
		MessageHandler messageHandler = messageHandlers.get(type);
		Logger.INSTANCE.debug("MessageHandlingService.handle : {\"type\": " + type + ", \"name\": " + (messageHandler != null ? messageHandler.getClass().getSimpleName() : null) + "}");
		Engine.INSTANCE.schedule(() -> {
			messageHandler.handle(connection, rawMessage);
			Logger.INSTANCE.debug("MessageHandlingService.handle : handled : {\"type\": " + type + ", \"name\": " + (messageHandler != null ? messageHandler.getClass().getSimpleName() : null) + "}");
		});
	}
}
