package sandbox.common.game.events;

import java.util.UUID;

import sandbox.common.math.position.Position;
import sandbox.engine.game.Event;
import sandbox.engine.math.CardinalOrientation;

public class Move implements Event {

	
	public static class Request implements Event {
		Move move;

		public Request(UUID moving, CardinalOrientation movement) {
			move = new Move(moving, movement);
		}
		
		private Request(UUID moving, CardinalOrientation movement, Position oldPosition, Position newPosition) {
			move = new Move(moving, movement, oldPosition, newPosition);
		}
		
		public Move move(Position oldPosition, Position newPosition) {
			return new Move(move.moving, move.movement, oldPosition, newPosition);
		}
		
		public UUID getMovingUUID() {
			return move.getMovingUUID();
		}
		
		public CardinalOrientation getCardinalOrientation() {
			return move.getCardinalOrientation();
		}
		
		public Position getOldPosition() {
			return move.getOldPosition();
		}
		
		public Position getNewPosition() {
			return move.getNewPosition();
		}

	}
	
	private UUID moving;
	private CardinalOrientation movement;
	private Position oldPosition;
	private Position newPosition;

	public Move(UUID moving, CardinalOrientation movement) {
		this.moving = moving;
		this.movement = movement;
	}

	private Move(UUID moving, CardinalOrientation movement, Position oldPosition, Position newPosition) {
		this.moving = moving;
		this.movement = movement;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
	}
	
	public Request request(Position oldPosition, Position newPosition) {
		return new Request(moving, movement, oldPosition, oldPosition);
	}
	
	public UUID getMovingUUID() {
		return moving;
	}
	
	public CardinalOrientation getCardinalOrientation() {
		return movement;
	}
	
	public Position getOldPosition() {
		return oldPosition;
	}
	
	public Position getNewPosition() {
		return newPosition;
	}
}
