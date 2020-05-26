package sandbox.engine.math;

import java.util.List;

public interface Orientation<E extends Orientation> {
	E getAny();
	E get();
	List<E> getValues();
}
