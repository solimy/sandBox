package sandbox.engine.game;

/**
 * Functionnal interface o/
 * 
 * this will actually work :
 * 
 * Script script = (entity) -> System.out.println(entity.toString());
 * 
 * @author daures_h
 *
 * @param <P>
 */
public interface Script<C> {
	void execute(C context);
	
	static final Script<?> EMPTY = (context) -> {
	};
}
