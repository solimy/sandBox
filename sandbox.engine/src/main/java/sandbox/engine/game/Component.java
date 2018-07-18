package sandbox.engine.game;

public interface Component {
	String getId();
	void onCreate(Entity attachedEntity);
	void onRemove(Entity attachedEntity);
	void onUpdate(Entity attachedEntity);
	void onRender(Entity attachedEntity);
	void onUse(Entity attachedEntity, Entity user);
	void onEvent(Entity attachedEntity, Event event);
}
