package sandbox.engine.network.message;

import java.util.function.Function;

public interface MessageAllocator<E extends Message<E, ?>, A> extends Function<A, E> {
}
