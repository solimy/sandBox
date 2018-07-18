package sandbox.engine;

import java.util.stream.Stream;

public class TestInterfaceFonctionnelle {

	public static interface ITest1<A, B, C, D, E> {
		A handle(B b, C c, D d, E e);
	}

	public static interface ITest2<A, B, C, D, E, F, G, H, I, J, K> {
		A handle(B b);
		// A handle(B b, C c); not compile if added
	}

	public static interface ITest3<A> {
		void handle(A... a);
	}

	public static void main(String[] args) {
		ITest1<String, Integer, Long, Float, Double> test1 = (i, l, f, d) -> i.toString() + " " + l.toString() + " "
				+ f.toString() + " " + d.toString();
		System.out.println("test1 : " + test1.handle(1, 2L, 3F, 4D));

		ITest2<String, String, ?, ?, ?, ?, ?, ?, ?, ?, ?> test2_1 = (b) -> b;

		ITest3<Integer> test3 = (iT -> System.out.println("test2 : " + Stream.of(iT).reduce((p, c) -> p + c).get()));
		test3.handle(1, 2, 3, 4);
	}
}
