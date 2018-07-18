package sandbox.engine.state;

public interface TimedState<E extends TimedState<E>> extends State {
	Long getMinimumDuration();
	Long getMaximumDuration();
	E getNextState();
	Boolean interruptedBy(E state);
}
