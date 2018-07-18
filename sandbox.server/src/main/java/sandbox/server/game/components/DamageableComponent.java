package sandbox.server.game.components;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Events;
import sandbox.common.math.position.Coordinates;
import sandbox.common.protocol.Messages;
import sandbox.common.world.Constraints;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.server.game.GameServer;

class DamageableComponent implements Component {
	public static final String ID = "DamageableComponent";
	final WeakReference<AtomicReference<Integer>> life;

	public DamageableComponent(WeakReference<AtomicReference<Integer>> life) {
		this.life = life;
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void onCreate(Entity attachedEntity) {

	}

	@Override
	public void onRemove(Entity attachedEntity) {

	}

	@Override
	public void onUpdate(Entity attachedEntity) {

	}

	@Override
	public void onRender(Entity attachedEntity) {

	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {

	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		if (event instanceof Damage) {
			onEventDamage(attachedEntity, (Damage) event);
		}
	}
	
	private void onEventDamage(Entity attachedEntity, Damage damage) {
		AtomicReference<Integer> life = this.life.get();
		if (life != null) {
			if (life.getAndAccumulate(damage.amount, (a, b)->a-b) > 0 && life.get() <= 0) {
				attachedEntity.trigger(Events.KILLED);
			}
		}
	}
}
