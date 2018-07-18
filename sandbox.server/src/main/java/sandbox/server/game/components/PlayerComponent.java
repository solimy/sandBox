package sandbox.server.game.components;

import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.server.game.GameServer;

public class PlayerComponent implements Component {
	public static String ID = "PlayerComponent";
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemove(Entity attachedEntity) {
		GameServer.INSTANCE.deletePlayer(attachedEntity);
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
		// TODO Auto-generated method stub
		
	}

}
