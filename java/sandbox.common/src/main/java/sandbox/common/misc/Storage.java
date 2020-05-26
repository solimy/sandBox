package sandbox.common.misc;

public class Storage<E> {
	protected final E[] storage;
	protected Integer currentIdex = 0;
	protected final Integer maxIndex;

	public Storage(E[] storage) {
		this.storage = storage;
		maxIndex = storage.length - 1;
	}

	public E[] getElements() {
		return storage;
	}

	public void putSlot(E toPut, Integer index) {
		storage[index % maxIndex] = toPut;
	}

	public void emptySlot(Integer index) {
		storage[index % maxIndex] = null;
	}
}
