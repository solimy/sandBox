package sandbox.engine.graphic.drawable.animation;

import java.util.HashMap;
import java.util.Map;

import sandbox.engine.Engine;
import sandbox.engine.graphic.drawable.sprite.Sprite;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Orientation;
import sandbox.engine.state.State;

public class Animator2D {
	private Map<Long, Map<Orientation, Animation>> animations = new HashMap<>();
	private Long currentAnimationId;
	private Map<Orientation, Animation> currentAnimation;
	private Map<Long, Long> animationSequence = new HashMap<>();
	Boolean loop = true;
	Long startTime = Engine.Clock.INSTANCE.getCurrentTimeMillis();

	public void addAnimation(Long animationIdentifier, CardinalOrientation orientation, Animation animation) {
		Map<Orientation, Animation> orientations = animations.get(animationIdentifier);
		if (orientations == null) {
			orientations = new HashMap<>();
			animations.put(animationIdentifier, orientations);
		}
		Map<Orientation, Animation> forientations = orientations;
		if (orientation.equals(orientation.getAny())) {
			orientation.getValues().forEach(value -> {
				forientations.put(value, animation);
			});
		} else
			orientations.put(orientation, animation);
	}

	public void play(Long animationId, Boolean loop) {
		this.loop = loop;
		Map<Orientation, Animation> newAnimation = animations.get(animationId);
		if (newAnimation == null) {
			return;
		}
		currentAnimation = newAnimation;
		currentAnimationId = animationId;
		startTime = Engine.Clock.INSTANCE.getCurrentTimeMillis();
	}

	public void setAnimationSequence(Long current, Long next) {
		animationSequence.put(current, next);
	}

	public Sprite getFrame(CardinalOrientation orientation) {
		if (currentAnimation == null)
			return Sprite.empty;
		Animation animation = currentAnimation.get(orientation);
		if (animation == null)
			return Sprite.empty;
		long ellapsedMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis() - startTime;
		if (ellapsedMillis > animation.getTotalDuration() && !loop) {
			play(animationSequence.getOrDefault(currentAnimationId, currentAnimationId), loop);
		}
		return animation.getFrame(ellapsedMillis);
	}
}
