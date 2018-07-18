package sandbox.common.game.events;

import java.util.UUID;

import sandbox.engine.game.Event;

public class Damage implements Event {
	public UUID source;
	public UUID destination;
	public Integer amount;

	public Damage(UUID source, UUID destination, Integer amount) {
		this.amount = amount;
		this.source = source;
		this.destination = destination;
	}
}
