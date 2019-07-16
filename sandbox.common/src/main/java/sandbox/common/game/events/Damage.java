package sandbox.common.game.events;

import sandbox.engine.misc.UUID;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;
import sandbox.engine.network.serialization.Serializer;

import java.nio.ByteBuffer;

import sandbox.engine.game.Event;

public class Damage implements Event, Serializable {
	public static final int TYPE = Damage.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		UUID source = (UUID) Serializer.deserializeNext(buffer);
		UUID destination = (UUID) Serializer.deserializeNext(buffer);
		Integer amount = buffer.getInt();
		return new Damage(source, destination, amount);
	};
	
	public UUID source;
	public UUID destination;
	public Integer amount;

	public Damage(UUID source, UUID destination, Integer amount) {
		this.amount = amount;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public ByteBuffer encodePayload() {
		ByteBuffer source = Serializer.serialize(this.source);
		ByteBuffer destination = Serializer.serialize(this.destination);
		return ByteBuffer
				.allocate(source.capacity() + destination.capacity() + Integer.BYTES)
				.put(source)
				.put(destination)
				.putInt(amount);
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
}
