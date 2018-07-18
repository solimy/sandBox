package sandbox.engine.misc.time;

public class Timer {
	Long delayMilli = 0L;
	Long startTime = System.currentTimeMillis();
	boolean isFinished = true;

	public void setDelay(Long currentTimeMillis, Long delayMilli) {
		this.delayMilli = delayMilli;
		reset(currentTimeMillis);
	}

	public void setDelay(Long delayMilli) {
		this.delayMilli = delayMilli;
	}

	public void reset(Long currentTimeMillis) {
		startTime = currentTimeMillis;
	}

	public boolean isFinished(Long currentTimeMillis) {
		return currentTimeMillis - startTime >= delayMilli;
	}
}
