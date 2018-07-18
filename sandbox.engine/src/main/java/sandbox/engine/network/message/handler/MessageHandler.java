package sandbox.engine.network.message.handler;

import sandbox.engine.network.Connection;
import sandbox.engine.network.message.Message;

public interface MessageHandler<E extends Message<?, ?>> {
	void handle(Connection connection, E message);
}
