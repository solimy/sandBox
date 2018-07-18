package sandbox.server.game.components;

import java.lang.ref.WeakReference;

import sandbox.common.misc.Spinner;
import sandbox.common.misc.Storage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;

class InventoryComponent implements Component {
	public static final String ID = "InventoryComponent";
	private final Storage<Entity> inventory = new Storage<>(new Entity[40]);
	private final WeakReference<Storage<Entity>> inventoryRef = new WeakReference<>(inventory);
	private final Spinner<Entity> itemShortcut = new Spinner<>(new Entity[4]);
	private final WeakReference<Spinner<Entity>> itemShortcutRef = new WeakReference<>(itemShortcut);
	
	public WeakReference<Storage<Entity>> getInventory() {
		return inventoryRef;
	}

	public WeakReference<Spinner<Entity>> getItemShortcut() {
		return itemShortcutRef;
	}
	@Override
	public String getId() {
		return null;
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
		if (event instanceof EntityUseActionMessage) {
			onEventEntityUseActionMessage(attachedEntity, (EntityUseActionMessage) event);
		}
	}
	
	private void onEventEntityUseActionMessage(Entity attachedEntity, EntityUseActionMessage useAction) {
		itemShortcut.getCurrent().use(attachedEntity);		
	}
}
