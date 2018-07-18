package sandbox.common.world.enums;

import java.util.HashMap;
import java.util.Map;

public enum EntityNature {
	ENVIRONMENTAL, FIREBALL, PLAYER;
	
	public static EntityNature getByOrdinal(Integer ordinal) {
		return entityNatures.get(ordinal);
	}
	
	private static final Map<Integer, EntityNature> entityNatures = new HashMap<>();
	static {
		for (int i = 0; i < EntityNature.values().length; i++) {
			entityNatures.put(EntityNature.values()[i].ordinal(), EntityNature.values()[i]);
		}
	}
}
