package sandbox.common.math.position;

import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.misc.Copyable;

public class Position implements Copyable<Position> {
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
}
