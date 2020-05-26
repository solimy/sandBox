package sandbox.common.misc;

import java.util.Vector;

public class Spinner<E> extends Storage<E> {
	private Integer currentIdex = 0;

	public Spinner(E[] storage) {
		super(storage);
	}

	public E getCurrent() {
		return storage[currentIdex];
	}

	public void spin() {
		currentIdex = (currentIdex + 1) % maxIndex;
	}
}
