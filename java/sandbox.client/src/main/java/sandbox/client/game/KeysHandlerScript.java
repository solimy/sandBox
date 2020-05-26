package sandbox.client.game;

import java.awt.event.KeyEvent;

import sandbox.client.ClientScript;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Move;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.game.Script;
import sandbox.engine.math.CardinalOrientation;

public enum KeysHandlerScript implements Script<KeyEvent> {
	INSTANCE;

	@Override
	public void execute(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_SPACE:
			ClientScript.INSTANCE.connection.send(new EntityUseActionMessage());
			break;
		case KeyEvent.VK_Z:
			break;
		case KeyEvent.VK_Q:
			break;
		case KeyEvent.VK_S:
			break;
		case KeyEvent.VK_D:
			break;
		case KeyEvent.VK_UP:
			if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
					.getStateManager().get().setStateIsAvailable())
				break;
			ClientScript.INSTANCE.connection
					.send(new EntityMoveMessage(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.NORTH)));
			break;
		case KeyEvent.VK_RIGHT:
			if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
					.getStateManager().get().setStateIsAvailable())
				break;
			ClientScript.INSTANCE.connection
					.send(new EntityMoveMessage(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.EAST)));
			break;
		case KeyEvent.VK_DOWN:
			if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
					.getStateManager().get().setStateIsAvailable())
				break;
			ClientScript.INSTANCE.connection
					.send(new EntityMoveMessage(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.SOUTH)));
			break;
		case KeyEvent.VK_LEFT:
			if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
					.getStateManager().get().setStateIsAvailable())
				break;
			ClientScript.INSTANCE.connection
					.send(new EntityMoveMessage(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.WEST)));
			break;
		default:
			break;
		}
	}
}
