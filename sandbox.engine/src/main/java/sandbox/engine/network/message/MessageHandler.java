package sandbox.engine.network.message;

import sandbox.engine.network.Connection;

public interface MessageHandler {
	void handle(Connection connection, RawMessage message);
}
