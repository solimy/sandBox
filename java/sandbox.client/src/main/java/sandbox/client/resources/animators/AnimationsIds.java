package sandbox.client.resources.animators;

public abstract class AnimationsIds {
	private static Long increment = -1L;
	public static Long IDLE = ++increment;
	public static Long WALK = ++increment;
	public static Long DEATH = ++increment;
	public static Long DEAD = ++increment;
}
