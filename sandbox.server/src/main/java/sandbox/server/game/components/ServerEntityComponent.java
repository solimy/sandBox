package sandbox.server.game.components;

import java.lang.ref.WeakReference;

import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Events;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.Messages;
import sandbox.common.world.Constraints;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.engine.Engine;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.math.Vector2D;
import sandbox.engine.network.message.Message;
import sandbox.engine.state.TimedStateManager;
import sandbox.server.game.GameServer;

public class ServerEntityComponent implements Component {
	public static final String ID = "ServerEntityComponent";

	final WeakReference<TimedStateManager<EntityState>> stateManagerRef;
	final WeakReference<Position> positionRef;

	public ServerEntityComponent(WeakReference<TimedStateManager<EntityState>> stateManager,
			WeakReference<Position> position) {
		this.stateManagerRef = stateManager;
		this.positionRef = position;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		Position position = this.positionRef.get();
		if (position != null) {
			GameServer.INSTANCE.broadcast(position.coordinates, Constraints.BROADCAST_RANGE,
					Messages.ENTITY_UPDATE.build(attachedEntity));
		}
	}

	@Override
	public void onRemove(Entity attachedEntity) {
		Position position = this.positionRef.get();
		if (position != null) {
			GameServer.INSTANCE.broadcast(position.coordinates, Constraints.BROADCAST_RANGE,
					Messages.ENTITY_REMOVE.build(attachedEntity.getUUID()));
		}
	}

	@Override
	public void onUpdate(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		if (Events.KILLED.equals(event)) {
			onEventKilled(attachedEntity, (Events) event);
		} else if (event instanceof Damage) {
			onEventDamage(attachedEntity, (Damage) event);
		} else if (event instanceof Move) {
			onEventMove(attachedEntity, (Move) event);
		} else if (Events.DESPAWN.equals(event)) {
			onEventDespawn(attachedEntity, (Events) event);
		}
	}

	private void onEventKilled(Entity attachedEntity, Events killed) {
		TimedStateManager<EntityState> stateManager = this.stateManagerRef.get();
		Position position = this.positionRef.get();
		if (stateManager != null && position != null) {
			stateManager.setState(EntityState.DEAD);
			GameServer.INSTANCE.broadcast(position.coordinates, Constraints.BROADCAST_RANGE,
					Messages.ENTITY_DEATH.build(attachedEntity.getUUID()));
		}
	}

	private void onEventDamage(Entity attachedEntity, Damage damage) {
		Position position = this.positionRef.get();
		if (position != null) {
			GameServer.INSTANCE.broadcast(position.coordinates, Constraints.BROADCAST_RANGE,
					Messages.DAMAGE_NOTIFICATION.build(damage));
		}
	}

	private void onEventMove(Entity attachedEntity, Move move) {
		Coordinates NW = null;
		Coordinates SE = null;
		Vector2D modNW = new Vector2D(-1, 1);
		Vector2D modSE = new Vector2D(1, -1);
		switch (move.getCardinalOrientation()) {
		case EAST:
		case SOUTH:
			NW = new Coordinates(move.getOldPosition().coordinates).modChunkCoordinates(modNW, null);
			SE = new Coordinates(move.getNewPosition().coordinates).modChunkCoordinates(modSE, null);
			break;
		case NORTH:
		case WEST:
			NW = new Coordinates(move.getNewPosition().coordinates).modChunkCoordinates(modNW, null);
			SE = new Coordinates(move.getOldPosition().coordinates).modChunkCoordinates(modSE, null);
			break;
		default:
			break;
		}
		GameServer.INSTANCE.broadcast(NW, SE, Messages.ENTITY_MOVE.build(move));
		System.out.println(Engine.Clock.INSTANCE.getCurrentTimeMillis() + " : sending movement ");
	}
	
	private void onEventDespawn(Entity attachedEntity, Events depsawn) {
		GameServer.INSTANCE.broadcast(positionRef.get().coordinates, Constraints.BROADCAST_RANGE,
				Messages.ENTITY_DEATH.build(attachedEntity.getUUID()));
		attachedEntity.remove();
	}

	@Override
	public String getId() {
		return ID;
	}
}
