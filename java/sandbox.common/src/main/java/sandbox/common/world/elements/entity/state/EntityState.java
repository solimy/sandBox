package sandbox.common.world.elements.entity.state;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sandbox.engine.game.Event;
import sandbox.engine.state.TimedState;

public enum EntityState implements TimedState<EntityState>{
	IDLE(0L, Long.MAX_VALUE, null), //
	DEAD(Long.MAX_VALUE, Long.MAX_VALUE, null, IDLE), //
	WALK(200L, 200L, IDLE, DEAD), //
	SPRINT(100L, 100L, IDLE, DEAD);

	final Long minimumDurationMillis;
	final Long totalDurationMillis;
	final EntityState nextState;
	final Set<TimedState<EntityState>> canInterrupt;

	private EntityState(Long minimumDurationMillis, Long totalDurationMillis, EntityState nextState,
			EntityState... canInterrupt) {
		this.minimumDurationMillis = minimumDurationMillis;
		this.totalDurationMillis = totalDurationMillis;
		this.nextState = nextState;
		this.canInterrupt = new HashSet<>(Arrays.asList(canInterrupt));
	}

	@Override
	public Long getMinimumDuration() {
		return minimumDurationMillis;
	}

	@Override
	public Long getMaximumDuration() {
		return totalDurationMillis;
	}

	@Override
	public EntityState getNextState() {
		return nextState;
	}

	@Override
	public Boolean interruptedBy(EntityState state) {
		return canInterrupt.contains(state);
	}
}
