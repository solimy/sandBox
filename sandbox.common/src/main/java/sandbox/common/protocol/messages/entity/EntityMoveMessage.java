package sandbox.common.protocol.messages.entity;

import sandbox.common.game.events.Move;
import sandbox.engine.game.Event;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class EntityMoveMessage extends ProtocolMessage implements Event {
	public static Integer TYPE = EntityMoveMessage.class.getName().hashCode();

	public final Move move;
	
	public EntityMoveMessage(Move move) {
		super(new RawMessage(TYPE, move));
		this.move = move;
	}


	public EntityMoveMessage(RawMessage rawMessage) {
		super(rawMessage);
		move = (Move) rawMessage.getWord(0);
	}
}
