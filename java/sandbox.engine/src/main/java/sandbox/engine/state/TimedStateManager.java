package sandbox.engine.state;

import java.util.Optional;

import sandbox.engine.Engine;

public class TimedStateManager<E extends TimedState<E>> implements StateManager<E> {
	private final E DEFAULT_STATE;
	private E currentState;
	private Boolean stateChangedBool = false;
	private Long lastSetStateTimeMillis = System.currentTimeMillis();

	public TimedStateManager(E defaulState) {
		DEFAULT_STATE = defaulState;
		currentState = DEFAULT_STATE;
	}

	private void stateAutoUpdate(Long currentTimeMillis) {
		if (currentTimeMillis - lastSetStateTimeMillis >= currentState.getMaximumDuration()) {
			currentState = Optional.ofNullable(currentState.getNextState()).orElse(DEFAULT_STATE);
			lastSetStateTimeMillis = currentTimeMillis;
			stateChangedBool = true;
		}
	}

	@Override
	public E getState() {
		stateAutoUpdate(Engine.Clock.INSTANCE.getCurrentTimeMillis());
		stateChangedBool = false;
		return currentState;
	}

	@Override
	synchronized public void setState(E state) {
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis(); 
		if (currentTimeMillis - lastSetStateTimeMillis >= currentState.getMinimumDuration()
				|| currentState.interruptedBy(state)) {
			currentState = state;
			stateChangedBool = true;
			lastSetStateTimeMillis = currentTimeMillis;
		}
	}

	@Override
	public boolean stateChanged() {
		stateAutoUpdate(Engine.Clock.INSTANCE.getCurrentTimeMillis());
		return stateChangedBool;
	}

	public Long getLastStateTimeMillis() {
		return lastSetStateTimeMillis;
	}

	public boolean setStateIsAvailable() {
		return Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastSetStateTimeMillis >= currentState.getMinimumDuration();
	}
	
	@Override
	public E getDefaultState() {
		return DEFAULT_STATE;
	}

}
