package sandbox.client.resources.animators;

import java.util.Arrays;
import java.util.HashMap;

import sandbox.client.resources.Ressources;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.engine.filesystem.SpriteSheet.ReadStyle;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.graphic.drawable.animation.Animator2D;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;

public class SkeletonAnimator {
	public static <E> HashMap<E, Animator2D> wrap(E key) {
		final Animator2D animator = new Animator2D();
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.SOUTH,
				new Animation(1000L, Ressources.INSTANCE.FILE_skeleton.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT,
						64, 64, new Vector2D(0, 2), new Vector2D(1, 2))));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.NORTH,
				new Animation(1000L, Ressources.INSTANCE.FILE_skeleton.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT,
						64, 64, new Vector2D(0, 0), new Vector2D(1, 0))));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.EAST,
				new Animation(1000L, Ressources.INSTANCE.FILE_skeleton.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT,
						64, 64, new Vector2D(0, 3), new Vector2D(1, 3))));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.WEST,
				new Animation(1000L, Ressources.INSTANCE.FILE_skeleton.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT,
						64, 64, new Vector2D(0, 1), new Vector2D(1, 1))));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.SOUTH,
				new Animation(EntityState.WALK.getMaximumDuration(), Ressources.INSTANCE.FILE_skeleton.extractAnimation(
						ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 10), new Vector2D(8, 10))));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.NORTH,
				new Animation(EntityState.WALK.getMaximumDuration(), Ressources.INSTANCE.FILE_skeleton.extractAnimation(
						ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 8), new Vector2D(8, 8))));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.EAST,
				new Animation(EntityState.WALK.getMaximumDuration(), Ressources.INSTANCE.FILE_skeleton.extractAnimation(
						ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 11), new Vector2D(8, 11))));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.WEST,
				new Animation(EntityState.WALK.getMaximumDuration(), Ressources.INSTANCE.FILE_skeleton.extractAnimation(
						ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 9), new Vector2D(8, 9))));
		animator.addAnimation(AnimationsIds.DEATH, CardinalOrientation.ANY, new Animation(1000L,
				Arrays.asList(Ressources.INSTANCE.FILE_terrain_atlas.extractSprite(15 * 32, 21 * 32, 32, 64))));
		animator.play(AnimationsIds.IDLE, true);
		animator.setAnimationSequence(AnimationsIds.WALK, AnimationsIds.IDLE);
		HashMap<E, Animator2D> keyed = new HashMap<>();
		keyed.put(key, animator);
		return keyed;
	}
}
