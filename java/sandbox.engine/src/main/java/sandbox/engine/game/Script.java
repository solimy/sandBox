package sandbox.engine.game;


/**
 * Functionnal interface o/
 * 
 * this will actually work :
 * 
 * Script script = (entity) -> Logger.INSTANCE.debug(entity.toString());
 * 
 * @author daures_h
 *
 * @param <P>
 */
public interface Script<C> {
	void execute(C context);
	
	static final Script<Void> EMPTY = (context) -> {
	};
}
