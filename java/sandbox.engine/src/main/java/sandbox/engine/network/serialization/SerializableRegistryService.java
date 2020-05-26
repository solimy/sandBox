package sandbox.engine.network.serialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sandbox.engine.logging.Logger;
import sandbox.engine.misc.Token;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.serialization.nativ.SerializableInteger;
import sandbox.engine.network.serialization.nativ.SerializableString;

public enum SerializableRegistryService {
	INSTANCE;

	private final Map<Integer, Deserializer> deserializers = new ConcurrentHashMap<Integer, Deserializer>();
	
	public void register(int type, Deserializer deserializer) {
		deserializers.put(type, deserializer);
		Logger.INSTANCE.debug("SerializableRegistryService: Registered {\"type\": " + type + ", \"name\": " + deserializer.getClass().getSimpleName() + "}");
	}
	
	public Deserializer get(int type) {
		return deserializers.get(type);
	}

	static {
		INSTANCE.register(UUID.TYPE, UUID.DESERIALIZER);
		INSTANCE.register(Token.TYPE, Token.DESERIALIZER);
		INSTANCE.register(SerializableString.TYPE, SerializableString.DESERIALIZER);
		INSTANCE.register(SerializableInteger.TYPE, SerializableInteger.DESERIALIZER);
	}
}
