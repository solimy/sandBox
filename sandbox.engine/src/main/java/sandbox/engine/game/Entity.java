package sandbox.engine.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import sandbox.engine.Engine;

public final class Entity implements Component {
	public static final Map<UUID, Entity> EntitiesMap = new ConcurrentHashMap<>();

	private final UUID uuid;
	private final Long creationTimeMillis;
	private String name = "";
	private final AtomicBoolean removed = new AtomicBoolean(false);

	private final Map<String, Component> components = new HashMap<String, Component>();

	public Entity(UUID uuid) {
		this.uuid = uuid;
		creationTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		Entity previous = EntitiesMap.put(uuid, this);
		if (previous != null) {
			previous.remove(true);
		}
		create();
	}

	public Entity(UUID uuid, Component...components) {
		this.uuid = uuid;
		creationTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		Entity previous = EntitiesMap.put(uuid, this);
		if (previous != null) {
			previous.remove(true);
		}
		for (Component component : components)
			addComponent(component.getId(), component);
		create();
	}

	public Entity update() {
		if (removed.get())
			return this;
		onUpdate(this);
		return this;
	}

	public Entity remove() {
		if (removed.getAndSet(true))
			return this;
		onRemove(this);
		EntitiesMap.remove(uuid);
		return this;
	}

	private Entity remove(Boolean alreadyRemovedFromMap) {
		if (removed.getAndSet(true))
			return this;
		onRemove(this);
		if (!alreadyRemovedFromMap)
			EntitiesMap.remove(uuid);
		return this;
	}

	public Entity create() {
		if (removed.get())
			return this;
		onCreate(this);
		return this;
	}

	public Entity render() {
		if (removed.get())
			return this;
		onRender(this);
		return this;
	}

	public Entity use(Entity user) {
		if (removed.get())
			return this;
		onUse(this, user);
		return this;
	}

	public Entity trigger(Event event) {
		if (removed.get())
			return this;
		onEvent(this, event);
		return this;
	}

	public Entity addComponent(String componentID, Component component) {
		components.put(componentID, component);
		return this;
	}

	public Component getComponent(String componentID) {
		return components.get(componentID);
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public Long getCreationTimeMillis() {
		return creationTimeMillis;
	}

	public String getName() {
		return name;
	}

	public Entity setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		components.values().forEach(component -> component.onCreate(attachedEntity));
	}

	@Override
	public void onRemove(Entity attachedEntity) {
		components.values().forEach(component -> component.onRemove(attachedEntity));
	}

	@Override
	public void onUpdate(Entity attachedEntity) {
		components.values().forEach(component -> component.onUpdate(attachedEntity));
	}

	@Override
	public void onRender(Entity attachedEntity) {
		components.values().forEach(component -> component.onRender(attachedEntity));
	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
		components.values().forEach(component -> component.onUse(attachedEntity, user));
	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		components.values().forEach(component -> component.onEvent(attachedEntity, event));
	}

	@Override
	public String getId() {
		return uuid.toString();
	}
}