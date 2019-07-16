package sandbox.engine.graphic.drawable.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sandbox.engine.graphic.drawable.sprite.Sprite;
import sandbox.engine.misc.Cloneable;

public class Animation implements Cloneable<Animation> {
	public static final Animation empty = new Animation(1000L, Arrays.asList(Sprite.empty));
	private final List<Sprite> frames = new ArrayList<>();
	private final long totalDurationMilli;
	private final int nbFrames;
	private final double frameDuration;

	public Animation(long totalDurationMilli, List<Sprite> frames) {
		this.totalDurationMilli = totalDurationMilli;
		this.nbFrames = frames.size();
		this.frames.addAll(frames);
		frameDuration = ((double)totalDurationMilli) / nbFrames;
	}

	public Sprite getFrame(long ellapsedMillis) {
		double d = (ellapsedMillis / frameDuration) % nbFrames;
		int frame = (int) d;
		return frames.get(frame);
	}

	public long getTotalDuration() {
		return totalDurationMilli;
	}
	
	@Override
	public Animation clone() {
		return new Animation(totalDurationMilli, frames);
	}
}
