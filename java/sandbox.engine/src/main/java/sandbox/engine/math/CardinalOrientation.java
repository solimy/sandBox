package sandbox.engine.math;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public enum CardinalOrientation implements Orientation<CardinalOrientation>, Serializable {
	NORTH, WEST, SOUTH, EAST, ANY;
	
	public static final int TYPE = CardinalOrientation.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		return getByOrdinal(buffer.getInt());
	};
	
	public static CardinalOrientation getByOrdinal(Integer ordinal) {
		return orientations.get(ordinal);
	}

	private static Map<Integer, CardinalOrientation> orientations = new HashMap<>();
	static {
		for (int i = 0; i < CardinalOrientation.values().length; i++) {
			orientations.put(CardinalOrientation.values()[i].ordinal(), CardinalOrientation.values()[i]);
		}
	}

	@Override
	public CardinalOrientation getAny() {
		return CardinalOrientation.ANY;
	}

	@Override
	public CardinalOrientation get() {
		return this;
	}

	@Override
	public List<CardinalOrientation> getValues() {
		return Arrays.asList(values());
	}

	@Override
	public ByteBuffer encodePayload() {
		return ByteBuffer.allocate(Integer.BYTES).putInt(ordinal());
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
}
