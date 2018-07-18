package sandbox.engine.math;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CardinalOrientation implements Orientation<CardinalOrientation> {
	NORTH, WEST, SOUTH, EAST, ANY;

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
}
