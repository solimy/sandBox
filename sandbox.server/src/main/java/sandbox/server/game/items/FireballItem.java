package sandbox.server.game.items;

import java.util.UUID;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.world.enums.EntityNature;
import sandbox.engine.Engine;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.misc.time.Timer;
import sandbox.server.game.components.ServerEntityComponent;
import sandbox.server.game.components.ai.LinearProjectileScript;

public class FireballItem implements Component {
	public static final String ID = "FireballItemScript";

	private Timer cooldown = new Timer();
	
	public FireballItem() {
		cooldown.setDelay(500L);
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
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		if (!cooldown.isFinished(currentTimeMillis))
			return;
		cooldown.reset(currentTimeMillis);
		WorldEntityComponent wec = new WorldEntityComponent(
				((WorldEntityComponent) user.getComponent(WorldEntityComponent.ID)).getPosition().get().copy().moveForward(1),
				EntityNature.FIREBALL);
		Entity fireball = new Entity(UUID.randomUUID(), wec, new ServerEntityComponent(wec.getStateManager(), wec.getPosition()),
				new LinearProjectileScript(user.getUUID(), 10000L, 8L, 10, wec.getStateManager(), wec.getPosition()));
	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
	}

	@Override
	public String getId() {
		return ID;
	}
}
