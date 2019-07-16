package sandbox.engine.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sandbox.engine.misc.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import sandbox.engine.Engine;

public final class Entity implements Component {
	public static final Map<UUID, Entity> EntitiesMap = new ConcurrentHashMap<>();

	private final UUID uuid;
	private final Long creationTimeMillis;
	private String name = "";
	private final AtomicBoolean removed = new AtomicBoolean(false);

	private final Map<String, Component> componentsMap = new HashMap<String, Component>();
	private final List<Component> componentsList = new ArrayList<Component>();

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
		componentsMap.put(componentID, component);
		componentsList.add(component);
		return this;
	}

	public Component getComponent(String componentID) {
		return componentsMap.get(componentID);
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
		for (Component component : componentsList)
			component.onCreate(attachedEntity);
	}

	@Override
	public void onRemove(Entity attachedEntity) {
		for (Component component : componentsList)
			component.onRemove(attachedEntity);
	}

	@Override
	public void onUpdate(Entity attachedEntity) {
		for (Component component : componentsList)
			component.onUpdate(attachedEntity);
	}

	@Override
	public void onRender(Entity attachedEntity) {
		for (Component component : componentsList)
			component.onRender(attachedEntity);
	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
		for (Component component : componentsList)
			component.onUse(attachedEntity, user);
	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		for (Component component : componentsList)
			component.onEvent(attachedEntity, event);
	}

	@Override
	public String getId() {
		return uuid.toString();
	}
}