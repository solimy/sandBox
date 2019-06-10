package sandbox.client.network.message.handler;

import sandbox.client.ClientScript;
import sandbox.client.game.components.GraphicEntityComponent;
import sandbox.client.game.render.CameraScript;
import sandbox.client.resources.animators.FireballAnimator;
import sandbox.client.resources.animators.SkeletonAnimator;
import sandbox.client.state.StateImpl;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.auth.AuthUUIDMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainMessage;
import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityRemoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.common.world.Constraints;
import sandbox.common.world.elements.entity.BodyPart;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.common.world.model.World;
import sandbox.engine.Engine;
import sandbox.engine.game.Entity;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.handler.MessageHandler;

public class ClientMessageHandler implements MessageHandler<Message<?, ?>> {

	@Override
	public void handle(Connection connection, Message<?, ?> message) {
		Messages messageType = Messages.getByMessageCode(message.type);
		System.out.println("[" + System.currentTimeMillis() + "] received : " + messageType.name());
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		switch (messageType) {
		case PING:
			ClientScript.INSTANCE.networkManager.connection.send(Messages.PONG.build(null));
			break;
		case PONG:
			ClientScript.INSTANCE.networkManager.connection.send(Messages.PING.build(null));
			break;
		case CHUNK_TERRAIN:
			ChunkTerrainMessage terrain = (ChunkTerrainMessage) message;
			World.INSTANCE.putChunkTerrain(terrain.attachment.coordinates, terrain.attachment);
			ClientScript.INSTANCE.networkManager.connection
					.send(Messages.CHUNK_ENTITIES_GET.build(terrain.attachment.coordinates));
			break;
		case ENTITY_UPDATE:
			EntityUpdateMessage update = (EntityUpdateMessage) message;
			Entity entity;

			WorldEntityComponent wec = (WorldEntityComponent) update.attachment.getComponent(WorldEntityComponent.ID);
			GraphicEntityComponent gec = (GraphicEntityComponent) update.attachment
					.getComponent(GraphicEntityComponent.ID);
			entity = (Entity) update.attachment;
			if (gec == null) {
				switch (wec.getNature().get()) {
				case FIREBALL:
					gec = new GraphicEntityComponent(wec.getPosition(), ClientScript.INSTANCE.context)
							.setAnimators(FireballAnimator.wrap(BodyPart.BODY));
					break;
				default:
					gec = new GraphicEntityComponent(wec.getPosition(), ClientScript.INSTANCE.context)
							.setAnimators(SkeletonAnimator.wrap(BodyPart.BODY));
					break;
				}
				entity.addComponent(GraphicEntityComponent.ID, gec);
			}
			Vector2D modifier = new Vector2D();
			if (entity.getUUID().equals(ClientScript.INSTANCE.uuid)) {
				ClientScript.INSTANCE.playerEntity = entity;
				for (modifier.y = -1; modifier.y <= 1; ++modifier.y) {
					for (modifier.x = -1; modifier.x <= 1; ++modifier.x) {
						Coordinates coordinates = new Coordinates(wec.getPosition().get().coordinates)
								.modChunkCoordinates(modifier, null);
						if (World.INSTANCE.getNullChunk(coordinates) == null) {
							ClientScript.INSTANCE.networkManager.connection
									.send(Messages.CHUNK_TERRAIN_GET.build(coordinates));
						}
					}
				}
				CameraScript.INSTANCE.center(wec.getPosition().get().coordinates);
			}
			World.INSTANCE.entityManager.put(wec.getPosition().get().coordinates, entity, false);
			break;
		case AUTH_UUID:
			AuthUUIDMessage uuid = (AuthUUIDMessage) message;
			ClientScript.INSTANCE.uuid = uuid.attachment;
			ClientScript.INSTANCE.networkManager.connection
					.send(Messages.ENTITY_UPDATE_GET.build(ClientScript.INSTANCE.uuid));
			ClientScript.INSTANCE.stateManager.setState(StateImpl.CONNECTED);
			break;
		case ENTITY_MOVE:
			EntityMoveMessage move = (EntityMoveMessage) message;
			Entity movingEntity = World.INSTANCE.entityManager.get(move.attachment.getMovingUUID());
			if (movingEntity == null) {
				ClientScript.INSTANCE.networkManager.connection
						.send(Messages.ENTITY_UPDATE_GET.build(move.attachment.getMovingUUID()));
				break;
			}
			movingEntity.trigger(move);
			break;
		case ENTITY_DEATH:
			EntityDeathMessage deathMessage = (EntityDeathMessage) message;
			entity = (Entity) World.INSTANCE.entityManager.get(deathMessage.attachment);
			if (entity != null)
				entity.trigger(deathMessage);
			break;
		case ENTITY_REMOVE:
			EntityRemoveMessage removeMessage = (EntityRemoveMessage) message;
			World.INSTANCE.entityManager.removeEntity(removeMessage.attachment);
			break;
		default:
			break;
		}
	}
}
