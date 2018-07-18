package sandbox.common.game.components;

import java.lang.ref.WeakReference;

import sandbox.common.game.events.Events;
import sandbox.common.game.events.Move;
import sandbox.common.game.events.Move.Request;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityRemoveMessage;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.common.world.enums.EntityNature;
import sandbox.common.world.model.World;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;
import sandbox.engine.state.TimedStateManager;

public class WorldEntityComponent implements Component {
	public static final String ID = "wec";
	private final Position position;
	private final WeakReference<Position> positionRef;
	private final EntityNature nature;
	private final WeakReference<EntityNature> natureRef;
	private final TimedStateManager<EntityState> stateManager = new TimedStateManager<EntityState>(EntityState.IDLE);
	private final WeakReference<TimedStateManager<EntityState>> stateManagerRef = new WeakReference<TimedStateManager<EntityState>>(
			stateManager);

	public WorldEntityComponent(Position position, EntityNature nature) {
		this.position = position;
		this.positionRef = new WeakReference<Position>(this.position);
		this.nature = nature;
		this.natureRef = new WeakReference<EntityNature>(this.nature);
	}

	public final Boolean isInChunkRange(WorldEntityComponent other, Integer rangeLimit) {
		System.out.println(position.coordinates.getChunkX() + " " + other.position.coordinates.getChunkX());
		return other == null ? false : position.coordinates.isInChunkRange(other.position.coordinates, rangeLimit);
	}

	public final WeakReference<Position> getPosition() {
		return positionRef;
	}

	public WeakReference<EntityNature> getNature() {
		return natureRef;
	}

	public WeakReference<TimedStateManager<EntityState>> getStateManager() {
		return stateManagerRef;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		World.INSTANCE.entityManager.put(position.coordinates, attachedEntity, true);
	}

	@Override
	public void onRemove(Entity attachedEntity) {
		World.INSTANCE.entityManager.removeEntity(attachedEntity);
		System.out.println("remove entity \"" + attachedEntity.getUUID() + "\" from World");
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
		if (event instanceof EntityRemoveMessage) {
			onEventEntityRemoveMessage(attachedEntity, (EntityDeathMessage) event);
		} else if (event instanceof EntityDeathMessage) {
			onEventEntityDeathMessage(attachedEntity, (EntityDeathMessage) event);
		} else if (event instanceof EntityMoveMessage) {
			onEventEntityMoveMessage(attachedEntity, (EntityMoveMessage) event);
		} else if (event instanceof Move.Request) {
			onEventEntityMoveRequest(attachedEntity, (Move.Request) event);
		}
	}

	private void onEventEntityRemoveMessage(Entity attachedEntity, EntityDeathMessage remove) {
		World.INSTANCE.entityManager.removeEntity(remove.attachment);
	}

	private void onEventEntityDeathMessage(Entity attachedEntity, EntityDeathMessage death) {
		stateManager.setState(EntityState.DEAD);
		attachedEntity.trigger(Events.KILLED);
	}

	private void onEventEntityMoveMessage(Entity attachedEntity, EntityMoveMessage move) {
		onEventEntityMoveRequest(attachedEntity, move.attachment.request(position, position.copy().moveForward(1)));
	}

	private void onEventEntityMoveRequest(Entity attachedEntity, Move.Request move) {
		Position oldPosition = position.copy();
		Entity movingEntity = World.INSTANCE.entityManager.move(move.getCardinalOrientation(), attachedEntity);
		if (movingEntity != null) {
			stateManager.setState(EntityState.WALK);
			attachedEntity.trigger(move.move(oldPosition, position));
		}
	}
	
	@Override
	public String getId() {
		return ID;
	}
}