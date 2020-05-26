package sandbox.engine.game;

public interface Component {
	void onCreate(Entity attachedEntity);
	void onDelete(Entity attachedEntity);
	void onUpdate(Entity attachedEntity);
	void onRender(Entity attachedEntity);
	void onUse(Entity attachedEntity, Entity user);
	void onEvent(Entity attachedEntity, Event event);
}
