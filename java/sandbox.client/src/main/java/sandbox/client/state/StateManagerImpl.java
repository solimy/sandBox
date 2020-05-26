package sandbox.client.state;

import sandbox.engine.state.StateManager;

public class StateManagerImpl implements StateManager<StateImpl> {

	StateImpl currentState = StateImpl.DISCONNECTED;
	Boolean stateChanged = true;

	@Override
	public StateImpl getState() {
		return currentState;
	}

	@Override
	public boolean stateChanged() {
		boolean toto = stateChanged;
		stateChanged = false;
		return toto;
	}

	@Override
	public void setState(StateImpl state) {
		currentState = state;
	}

	@Override
	public StateImpl getDefaultState() {
		return null;
	}
}
