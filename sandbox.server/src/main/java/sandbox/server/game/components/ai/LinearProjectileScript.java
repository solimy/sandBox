package sandbox.server.game.components.ai;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.UUID;

import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Events;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.Messages;
import sandbox.common.world.Constraints;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.common.world.model.World;
import sandbox.engine.Engine;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.state.TimedStateManager;
import sandbox.server.game.GameServer;

public class LinearProjectileScript implements AI, Component {
	public static final String ID = "LinearProjectileScript";

	final UUID owner;
	final Long TTL;
	Long DTT;
	Long deathTime = null;
	final Integer power;
	final WeakReference<TimedStateManager<EntityState>> stateManagerRef;
	final WeakReference<Position> positionRef;

	public LinearProjectileScript(UUID owner, Long timeToLiveMillis, Long distanceToTravel, Integer power,
			WeakReference<TimedStateManager<EntityState>> stateManagerRef, final WeakReference<Position> positionRef) {
		this.stateManagerRef = stateManagerRef;
		this.positionRef = positionRef;
		this.power = power;
		this.owner = owner;
		TTL = timeToLiveMillis;
		DTT = distanceToTravel;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
	}

	@Override
	public void onRemove(Entity attachedEntity) {
	}

	@Override
	public void onUpdate(Entity attachedEntity) {
		Long time = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		EntityState currentState = stateManagerRef.get().getState();

		if (EntityState.DEAD.equals(currentState)
				&& Engine.Clock.INSTANCE.getCurrentTimeMillis() - deathTime >= AI.PROJECTILE_DESPAWN_TIME) {
			attachedEntity.trigger(Events.DESPAWN);
			return;
		}

		if (!stateManagerRef.get().setStateIsAvailable())
			return;

		if (attachedEntity.getCreationTimeMillis() - time >= TTL || DTT == 0) {
			System.out.println(Events.KILLED.name());
			attachedEntity.trigger(Events.KILLED);
			return;
		}

		Collection<Entity> damageables = World.INSTANCE.getCell(positionRef.get().coordinates).getDynamicEntities()
				.values();
		if (damageables.size() > 1) {
			damageables.forEach(entity -> entity.trigger(new Damage(owner, entity.getUUID(), power)));
			attachedEntity.trigger(Events.KILLED);
			return;
		}
		attachedEntity.trigger(new Move.Request(attachedEntity.getUUID(), positionRef.get().orientation));
	}

	@Override
	public void onRender(Entity attachedEntity) {
	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		if (Events.KILLED.equals(event)) {
			onEventKilled(attachedEntity, (Events) event);
		} else if (event instanceof Move) {
			onEventMove(attachedEntity, (Move) event);
		}
	}

	private void onEventKilled(Entity attachedEntity, Events killed) {
		deathTime = Engine.Clock.INSTANCE.getCurrentTimeMillis();
	}

	private void onEventMove(Entity attachedEntity, Move move) {
		--DTT;
	}

	@Override
	public String getId() {
		return ID;
	}
}
