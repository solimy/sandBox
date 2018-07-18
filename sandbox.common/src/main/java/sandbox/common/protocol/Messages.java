package sandbox.common.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Coordinates;
import sandbox.common.misc.Token;
import sandbox.common.protocol.messages.auth.AuthConnectMessage;
import sandbox.common.protocol.messages.auth.AuthUUIDMessage;
import sandbox.common.protocol.messages.chunk.ChunkEntitiesGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainGetMessage;
import sandbox.common.protocol.messages.chunk.ChunkTerrainMessage;
import sandbox.common.protocol.messages.combat.DamageNotificationMessage;
import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.protocol.messages.entity.EntityMoveMessage;
import sandbox.common.protocol.messages.entity.EntityRemoveMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateGetMessage;
import sandbox.common.protocol.messages.entity.EntityUpdateMessage;
import sandbox.common.protocol.messages.entity.EntityUseActionMessage;
import sandbox.common.protocol.messages.misc.MiscSoundEffectEventMessage;
import sandbox.common.protocol.messages.misc.MiscVisualEffectEventMessage;
import sandbox.common.protocol.messages.test.TestPingMessage;
import sandbox.common.protocol.messages.test.TestPongMessage;
import sandbox.common.world.model.Chunk;
import sandbox.engine.game.Entity;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public enum Messages {
	/*
	 * TEST
	 */
	PING(TestPingMessage.type, (MessageAllocator<TestPingMessage, Object>) (notUsed) -> TestPingMessage.allocator.apply(notUsed)),
	//
	PONG(TestPongMessage.type, (MessageAllocator<TestPongMessage, Object>) (notUsed) -> TestPongMessage.allocator.apply(notUsed)),
	//
	/*
	 * chunk
	 */
	CHUNK_TERRAIN_GET(ChunkTerrainGetMessage.type,
			(MessageAllocator<ChunkTerrainGetMessage, Object>) (coordinates) -> ChunkTerrainGetMessage.allocator
					.apply((Coordinates) coordinates)),
	//
	CHUNK_TERRAIN(ChunkTerrainMessage.type,
			(MessageAllocator<ChunkTerrainMessage, Object>) (chunk) -> ChunkTerrainMessage.allocator.apply((Chunk) chunk)),
	//
	CHUNK_ENTITIES_GET(ChunkEntitiesGetMessage.type,
			(MessageAllocator<ChunkEntitiesGetMessage, Object>) (coordinates) -> ChunkEntitiesGetMessage.allocator
					.apply((Coordinates) coordinates)),
	//
	/*
	 * auth
	 */
	AUTH_CONNECT(AuthConnectMessage.type,
			(MessageAllocator<AuthConnectMessage, Object>) (token) -> AuthConnectMessage.allocator.apply((Token) token)),
	//
	AUTH_UUID(AuthUUIDMessage.type,
			(MessageAllocator<AuthUUIDMessage, Object>) (uuid) -> AuthUUIDMessage.allocator.apply((UUID) uuid)),
	//
	/*
	 * player
	 */
	ENTITY_UPDATE_GET(EntityUpdateGetMessage.type,
			(MessageAllocator<EntityUpdateGetMessage, Object>) (uuid) -> EntityUpdateGetMessage.allocator
					.apply((UUID) uuid)),
	//
	ENTITY_UPDATE(EntityUpdateMessage.type,
			(MessageAllocator<EntityUpdateMessage, Object>) (entity) -> EntityUpdateMessage.allocator.apply((Entity) entity)),
	//
	ENTITY_MOVE(EntityMoveMessage.type,
			(MessageAllocator<EntityMoveMessage, Object>) (move) -> EntityMoveMessage.allocator
					.apply((Move) move)),
	//
	ENTITY_USE_ACTION(EntityUseActionMessage.type,
			(MessageAllocator<EntityUseActionMessage, Object>) (notUsed) -> EntityUseActionMessage.allocator.apply(notUsed)),
	//
	ENTITY_DEATH(EntityDeathMessage.type,
			(MessageAllocator<EntityDeathMessage, Object>) (uuid) -> EntityDeathMessage.allocator.apply((UUID) uuid)),
	//
	ENTITY_REMOVE(EntityRemoveMessage.type,
			(MessageAllocator<EntityRemoveMessage, Object>) (uuid) -> EntityRemoveMessage.allocator.apply((UUID) uuid)),
	//

	//
	/*
	 * combat
	 */
	DAMAGE_NOTIFICATION(DamageNotificationMessage.type,
			(MessageAllocator<DamageNotificationMessage, Object>) (damage) -> DamageNotificationMessage.allocator
					.apply((Damage) damage)),
	//

	//
	/*
	 * misc
	 */
	VISUAL_EFFECT_EVENT(MiscVisualEffectEventMessage.type,
			(MessageAllocator<MiscVisualEffectEventMessage, Object>) (effectId) -> MiscVisualEffectEventMessage.allocator
					.apply((Integer) effectId)),
	//
	SOUND_EFFECT_EVENT(MiscSoundEffectEventMessage.type,
			(MessageAllocator<MiscSoundEffectEventMessage, Object>) (effectId) -> MiscSoundEffectEventMessage.allocator
					.apply((Integer) effectId)),
	//
	;

	private final Integer type;
	private final MessageAllocator<? extends Message<?, ?>, Object> allocator;

	private Messages(Integer type, MessageAllocator<? extends Message<?, ?>, Object> allocator) {
		this.type = type;
		this.allocator = allocator;
	}
	
	public Message<? extends Message<?, ?>, ?> build(Object messageContent) {
		System.out.println("building message of type : " + getByMessageCode(type).name());
		return allocator.apply(messageContent);
	}
	
	public Integer getType() {
		return type;
	}

	///////////////////////
	public static Messages getByMessageCode(Integer messageCode) {
		return mapping.get(messageCode);
	}

	private static Map<Integer, Messages> mapping = new HashMap<>();
	static {
		for (Messages type : values())
			mapping.put(type.type, type);
	}
	///////////////////////
}
