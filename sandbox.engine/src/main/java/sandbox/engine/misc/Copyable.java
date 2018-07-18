package sandbox.engine.misc;

public interface Copyable<T> {
	T copy(T toCopy);
	T copy();
}
