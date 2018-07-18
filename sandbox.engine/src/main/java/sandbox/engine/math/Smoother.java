package sandbox.engine.math;

public class Smoother {
	Integer start = 0;
	Integer end = 0;
	Long startTimeMillis;
	Long durationMilli = 0L;
	Long coef;
	Long time = 0L;
	boolean done = true;
	Integer value = 0;

	public void smooth(Integer start, Integer end, Long startTimeMillis, Long durationMilli) {
		this.start = start;
		this.end = end;
		this.startTimeMillis = startTimeMillis;
		this.durationMilli = durationMilli;
		coef = ((end - start)*1000) / durationMilli;
		done = false;
		time = 0L;
		value = start;
	}
	
	public Integer get(Long currentTimeMillis) {
		if (done) {
			return value;
		}
		time = currentTimeMillis - startTimeMillis;
		if (time >= durationMilli) {
			done = true;
			value = end;
			return value;
		}
		value = start + (new Long(coef * time).intValue()/1000);
		return value;
	}
}
