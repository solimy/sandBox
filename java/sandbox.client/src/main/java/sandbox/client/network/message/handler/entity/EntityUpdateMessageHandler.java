package sandbox.client.network.message.handler.entity;

import sandbox.client.ClientScript;
import sandbox.client.game.components.GraphicEntityComponent;
import sandbox.client.game.render.CameraScript;
import sandbox.client.resources.animators.FireballAnimator;
import sandbox.client.resources.animators.SkeletonAnimator;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.common.world.elements.entity.BodyPart;
import sandbox.common.world.model.World;
import sandbox.engine.game.Entity;
import sandbox.engine.logging.Logger;
import sandbox.engine.math.Vector2D;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.MessageHandler;
import sandbox.engine.network.message.MessageHandlingService;
import sandbox.engine.network.message.RawMessage;

public enum EntityUpdateMessageHandler implements MessageHandler {
	INSTANCE;

	@Override
	public void handle(Connection connection, RawMessage message) {
		EntityUpdateMessage update = new EntityUpdateMessage(message);
		Entity entity;
		Logger.INSTANCE.debug("Updating entity with uuid : {" + update.uuid + "}");
		entity = World.INSTANCE.entityManager.get(update.uuid);
		WorldEntityComponent wec = update.worldEntityComponent;
		if (entity == null) {
			Logger.INSTANCE.debug("Creating new entity with uuid : {" + update.uuid + "}");
			entity = new Entity(update.uuid);
			entity.addComponent(WorldEntityComponent.ID, wec);
			GraphicEntityComponent gec = null;
			switch (wec.getNature().get()) {
				case FIREBALL:
					gec = new GraphicEntityComponent(wec.getPosition())
							.setAnimators(FireballAnimator.wrap(BodyPart.BODY));
					break;
				default:
					gec = new GraphicEntityComponent(wec.getPosition())
							.setAnimators(SkeletonAnimator.wrap(BodyPart.BODY));
					break;
			}
			entity.addComponent(GraphicEntityComponent.ID, gec);
		}
		World.INSTANCE.entityManager.put(wec.getPosition().get().coordinates, entity, true);
		Vector2D modifier = new Vector2D();
		if (entity.getUUID().equals(ClientScript.INSTANCE.uuid)) {
			ClientScript.INSTANCE.playerEntity = entity;
			Logger.INSTANCE.debug("Updating client entity");
			for (modifier.y = -1; modifier.y <= 1; ++modifier.y) {
				for (modifier.x = -1; modifier.x <= 1; ++modifier.x) {
					Coordinates coordinates = new Coordinates(wec.getPosition().get().coordinates)
							.modChunkCoordinates(modifier, null);
					if (World.INSTANCE.getNullChunk(coordinates) == null) {
						ClientScript.INSTANCE.connection
								.send(new ChunkTerrainGetMessage(coordinates));
					}
				}
			}
			CameraScript.INSTANCE.center(wec.getPosition().get().coordinates);
		}
		World.INSTANCE.entityManager.put(wec.getPosition().get().coordinates, entity, false);
	}
}
