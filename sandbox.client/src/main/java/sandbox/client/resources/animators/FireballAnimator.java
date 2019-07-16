package sandbox.client.resources.animators;

import java.util.HashMap;
import java.util.stream.Collectors;

import sandbox.client.resources.Ressources;
import sandbox.engine.filesystem.SpriteSheet.ReadStyle;
import sandbox.engine.graphic.drawable.animation.Animation;
import sandbox.engine.graphic.drawable.animation.Animator2D;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;

public class FireballAnimator {
	public static final Integer spriteSize = 512 / 8;
	public static final Integer expSpriteSize = 256 / 4;
	private static final int size = 2000;
	private static final int xOrigin = -26;
	private static final int yOrigin = -40;
	
	public static <E> HashMap<E, Animator2D> wrap(E key) {
		final Animator2D animator = new Animator2D();
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.SOUTH,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 6), new Vector2D(7, 6))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.SOUTH,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 6), new Vector2D(7, 6))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.NORTH,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 2), new Vector2D(7, 2))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.NORTH,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 2), new Vector2D(7, 2))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.EAST,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 4), new Vector2D(7, 4))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.EAST,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 4), new Vector2D(7, 4))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.IDLE, CardinalOrientation.WEST,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 0), new Vector2D(7, 0))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.WALK, CardinalOrientation.WEST,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_fireball
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, spriteSize, spriteSize,
										new Vector2D(0, 0), new Vector2D(7, 0))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.addAnimation(AnimationsIds.DEATH, CardinalOrientation.ANY,
				new Animation(1000L,
						Ressources.INSTANCE.FILE_explosion
								.extractAnimation(ReadStyle.UP_LEFT_TO_DOWN_RIGHT, expSpriteSize, expSpriteSize,
										new Vector2D(0, 0), new Vector2D(3, 3))
								.stream().map(sprite -> sprite.setOrigin(xOrigin, yOrigin)).map(sprite -> sprite.setSize(size, size)).collect(Collectors.toList())));
		animator.play(AnimationsIds.IDLE, true);
		animator.setAnimationSequence(AnimationsIds.WALK, AnimationsIds.IDLE);
		animator.setAnimationSequence(AnimationsIds.IDLE, AnimationsIds.IDLE);
		HashMap<E, Animator2D> keyed = new HashMap<>();
		keyed.put(key, animator);
		return keyed;
	}
}
