package sandbox.engine.state;

public interface StateManager<E extends State> {
	E getState();
	E getDefaultState();
	void setState(E state);
	boolean stateChanged();
}
