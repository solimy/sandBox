package sandbox.server.game.components;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Events;
import sandbox.common.misc.Spinner;
import sandbox.common.misc.Storage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;

public class RPGComponent implements Component {
	public static final String ID = "RPGEntityComponent";
	final AtomicReference<Integer> level = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> levelRef = new WeakReference<AtomicReference<Integer>>(level);
	final AtomicReference<Integer> maxLife = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> maxLifeRef = new WeakReference<AtomicReference<Integer>>(maxLife);
	final AtomicReference<Integer> life = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> lifeRef = new WeakReference<AtomicReference<Integer>>(life);
	final AtomicReference<Integer> maxEndurance = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> maxEnduranceRef = new WeakReference<AtomicReference<Integer>>(
			maxEndurance);
	final AtomicReference<Integer> endurance = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> enduranceRef = new WeakReference<AtomicReference<Integer>>(endurance);
	final AtomicReference<Integer> maxMana = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> maxManaRef = new WeakReference<AtomicReference<Integer>>(maxMana);
	final AtomicReference<Integer> mana = new AtomicReference<Integer>();
	final WeakReference<AtomicReference<Integer>> manaRef = new WeakReference<AtomicReference<Integer>>(mana);
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

	public RPGComponent(Integer level, Integer maxLife, Integer maxEndurance, Integer maxMana) {
		this.level.set(level);
		this.maxLife.set(maxLife);
		this.life.set(maxLife);
		this.maxEndurance.set(maxEndurance);
		this.endurance.set(maxEndurance);
		this.maxMana.set(maxMana);
		this.mana.set(maxMana);
	}

	public WeakReference<AtomicReference<Integer>> getLevel() {
		return levelRef;
	}

	public WeakReference<AtomicReference<Integer>> getLife() {
		return lifeRef;
	}

	public WeakReference<AtomicReference<Integer>> getEndurance() {
		return enduranceRef;
	}

	public WeakReference<AtomicReference<Integer>> getMana() {
		return manaRef;
	}

	public WeakReference<AtomicReference<Integer>> getMaxLife() {
		return maxLifeRef;
	}

	public WeakReference<AtomicReference<Integer>> getMaxEndurance() {
		return maxEnduranceRef;
	}

	public WeakReference<AtomicReference<Integer>> getMaxMana() {
		return maxManaRef;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemove(Entity attachedEntity) {
		// TODO Auto-generated method stub

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
		if (event instanceof EntityUseActionMessage) {
			onEventEntityUseActionMessage(attachedEntity, (EntityUseActionMessage) event);
		} else if (event instanceof Damage) {
			onEventDamage(attachedEntity, (Damage) event);
		}
	}

	private void onEventEntityUseActionMessage(Entity attachedEntity, EntityUseActionMessage useAction) {
		itemShortcut.getCurrent().use(attachedEntity);
	}

	private void onEventDamage(Entity attachedEntity, Damage damage) {
		Integer prevLife = life.getAndAccumulate(damage.amount, (a, b) -> a - b);
		if (prevLife > 0 && life.get() <= 0) {
			attachedEntity.trigger(Events.KILLED);
		}
	}
}
