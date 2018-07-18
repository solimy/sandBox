package sandbox.client.game;

import javafx.scene.input.KeyEvent;
import sandbox.client.ClientScript;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Move;
import sandbox.common.protocol.Messages;
import sandbox.engine.Engine;
import sandbox.engine.game.Script;
import sandbox.engine.math.CardinalOrientation;

public enum KeysHandlerScript implements Script<KeyEvent> {
	INSTANCE;

	@Override
	public void execute(KeyEvent event) {
		switch (event.getEventType().getName()) {
		case "KEY_PRESSED":
			switch (event.getCode()) {
			case ESCAPE:
				System.exit(0);
				break;
			case SPACE:
				ClientScript.INSTANCE.networkManager.connection.send(Messages.ENTITY_USE_ACTION.build(null));
				break;
			case Z:
				break;
			case Q:
				break;
			case S:
				break;
			case D:
				break;
			case UP:
				if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
						.getStateManager().get().setStateIsAvailable())
					break;
				ClientScript.INSTANCE.networkManager.connection.send(
						Messages.ENTITY_MOVE.build(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.NORTH)));
				break;
			case RIGHT:
				if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
						.getStateManager().get().setStateIsAvailable())
					break;
				ClientScript.INSTANCE.networkManager.connection.send(
						Messages.ENTITY_MOVE.build(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.EAST)));
				break;
			case DOWN:
				if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
						.getStateManager().get().setStateIsAvailable())
					break;
				ClientScript.INSTANCE.networkManager.connection.send(
						Messages.ENTITY_MOVE.build(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.SOUTH)));
				break;
			case LEFT:
				if (!((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
						.getStateManager().get().setStateIsAvailable())
					break;
				ClientScript.INSTANCE.networkManager.connection.send(
						Messages.ENTITY_MOVE.build(new Move(ClientScript.INSTANCE.uuid, CardinalOrientation.WEST)));
				break;
			default:
				break;
			}
			break;
		case "KEY_RELEASED":
			break;
		case "KEY_TYPED":
			break;
		default:
			break;
		}
	}
}
