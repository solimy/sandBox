package sandbox.engine.misc.unsafe;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
	@Override
	default void run() {
		try {
			tryRun();
		} catch (final Throwable t) {
			System.err.println("ThrowingRunnable::run : " + t.toString() + " - " + t.getMessage());
			t.printStackTrace();
		}
	}

	void tryRun() throws Throwable;
}