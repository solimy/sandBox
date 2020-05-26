package sandbox.common.math.position;

import java.nio.ByteBuffer;

import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.misc.Copyable;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;
import sandbox.engine.network.serialization.Serializer;

public class Position implements Copyable<Position>, Serializable {
	public static final int TYPE = Position.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		Coordinates coordinates = (Coordinates) Serializer.deserializeNext(buffer);
		CardinalOrientation orientation = (CardinalOrientation) Serializer.deserializeNext(buffer);
		return new Position(coordinates, orientation);
	};
	
	public Coordinates coordinates;
	public CardinalOrientation orientation;
	
	public Position(Coordinates coordinates, CardinalOrientation orientation) {
		this.coordinates = new Coordinates(coordinates);
		this.orientation = orientation;
	}
	
	@Override
	public Position copy(Position position) {
		coordinates.copy(position.coordinates);
		orientation = position.orientation;
		return this;
	}
	
	public Position moveForward(Integer amount) {
		coordinates.worldTranslation(orientation, amount);
		return this;
	}

	@Override
	public Position copy() {
		return new Position(coordinates, orientation);
	}
	
	@Override
	public String toString() {
		return orientation.name() + " : " + coordinates.toString();
	}

	@Override
	public ByteBuffer encodePayload() {
		ByteBuffer cooridinates = Serializer.serialize(this.coordinates);
		ByteBuffer orientation = Serializer.serialize(this.orientation);
		return ByteBuffer
				.allocate(cooridinates.capacity() + orientation.capacity())
				.put(cooridinates)
				.put(orientation);
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
}
