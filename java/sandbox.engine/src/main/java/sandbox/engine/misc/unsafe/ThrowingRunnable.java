package sandbox.engine.misc.unsafe;

import sandbox.engine.logging.Logger;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
	@Override
	default void run() {
		try {
			tryRun();
		} catch (final Throwable t) {
			Logger.INSTANCE.error("ThrowingRunnable::run : " + t.toString() + " - " + t.getMessage());
			t.printStackTrace();
		}
	}

	void tryRun() throws Throwable;
}