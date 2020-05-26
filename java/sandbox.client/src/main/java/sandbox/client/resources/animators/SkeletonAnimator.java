package sandbox.client.resources.animators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import sandbox.client.resources.Ressources;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.engine.filesystem.SpriteSheet.ReadStyle;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.graphic.drawable.animation.Animator2D;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;

public class SkeletonAnimator {
	
	private static final int xOrigin = -18;
	private static final int yOrigin = -50;
	private static final int width = 1500;
	private static final int height = 1500;
	
	public static <E> HashMap<E, Animator2D> wrap(E key) {
		final Animator2D animator = new Animator2D();
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.SOUTH,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 2),
										new Vector2D(1, 2))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.NORTH,
				new Animation(1500L,
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 0),
										new Vector2D(1, 0))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.EAST,
				new Animation(1500L,
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 3),
										new Vector2D(1, 3))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.WEST,
				new Animation(1500L,
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 1),
										new Vector2D(1, 1))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.SOUTH,
				new Animation(EntityState.WALK.getMaximumDuration(),
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 10),
										new Vector2D(8, 10))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.NORTH,
				new Animation(EntityState.WALK.getMaximumDuration(),
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 8),
										new Vector2D(8, 8))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.EAST,
				new Animation(EntityState.WALK.getMaximumDuration(),
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 11),
										new Vector2D(8, 11))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.WEST,
				new Animation(EntityState.WALK.getMaximumDuration(),
						Ressources.INSTANCE.FILE_skeleton
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, 64, 64, new Vector2D(0, 9),
										new Vector2D(8, 9))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(width, height)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.DEATH, CardinalOrientation.ANY,
				new Animation(1000L, Arrays.asList(Ressources.INSTANCE.SPRITE_ENTITY_TOMBSTONE)));
		animator.addAnimation(AnimationsIds.DEAD, CardinalOrientation.ANY,
				new Animation(1000L, Arrays.asList(Ressources.INSTANCE.SPRITE_ENTITY_TOMBSTONE)));
		animator.play(AnimationsIds.IDLE, true);
		animator.setAnimationSequence(AnimationsIds.WALK, AnimationsIds.IDLE);
		animator.setAnimationSequence(AnimationsIds.IDLE, AnimationsIds.IDLE);
		animator.setAnimationSequence(AnimationsIds.DEATH, AnimationsIds.DEAD);
		animator.setAnimationSequence(AnimationsIds.DEAD, AnimationsIds.DEAD);
		HashMap<E, Animator2D> keyed = new HashMap<>();
		keyed.put(key, animator);
		return keyed;
	}
}
