package sandbox.engine.graphic.drawable.animation;

import java.util.ArrayList;
import java.util.List;

import sandbox.engine.Engine;
import sandbox.engine.graphic.drawable.sprite.Sprite;
import sandbox.engine.misc.Cloneable;

public class Animation implements Cloneable<Animation> {
	private final List<Sprite> frames = new ArrayList<>();
	private final Long totalDurationMilli;
	private final Integer nbFrames;
	private final Long frameDuration;

	public Animation(Long totalDurationMilli, List<Sprite> frames) {
		this.totalDurationMilli = totalDurationMilli;
		this.nbFrames = frames.size();
		this.frames.addAll(frames);
		frameDuration = totalDurationMilli / nbFrames;
	}

	public Sprite getFrame(Long ellapsedMillis) {
		return frames.get(
				Long.valueOf((ellapsedMillis / frameDuration) % nbFrames)
						.intValue());
	}

	public Long getTotalDuration() {
		return totalDurationMilli;
	}
	
	@Override
	public Animation clone() {
		return new Animation(totalDurationMilli, frames);
	}
}
