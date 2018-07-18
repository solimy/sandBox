package sandbox.common.protocol.messages.entity;

import java.nio.ByteBuffer;
import java.util.UUID;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.misc.serializer.CoordinatesSerializer;
import sandbox.common.misc.serializer.EntityNatureSerializer;
import sandbox.common.misc.serializer.OrientationSerializer;
import sandbox.common.misc.serializer.StringSerializer;
import sandbox.common.misc.serializer.UUIDSerializer;
import sandbox.common.world.enums.EntityNature;
import sandbox.common.world.model.Chunk;
import sandbox.engine.game.Entity;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.network.message.Message;
import sandbox.engine.network.message.MessageAllocator;

public class EntityUpdateMessage extends Message<EntityUpdateMessage, Entity> {

	protected EntityUpdateMessage(Entity entity) {
		super(type, entity);
	}

	@Override
	protected ByteBuffer encode() {
		ByteBuffer uuid = UUIDSerializer.INSTANCE.encode(attachment.getUUID());
		uuid.flip();
		WorldEntityComponent ces = (WorldEntityComponent) attachment.getComponent(WorldEntityComponent.ID);
		ByteBuffer nature = EntityNatureSerializer.INSTANCE.encode(ces.getNature().get());
		nature.flip();
		ByteBuffer coordinates = CoordinatesSerializer.INSTANCE.encode(ces.getPosition().get().coordinates);
		coordinates.flip();
		ByteBuffer orientation = OrientationSerializer.INSTANCE.encode(ces.getPosition().get().orientation);
		orientation.flip();
		ByteBuffer name = StringSerializer.INSTANCE.encode(attachment.getName());
		name.flip();
		return ByteBuffer.allocate(coordinates.remaining() + orientation.remaining() + uuid.remaining()
				+ name.remaining() + nature.remaining()).put(uuid).put(nature).put(coordinates).put(orientation)
				.put(name);
	}

	@Override
	protected void decode(ByteBuffer inputBuffer) {
		UUID uuid = UUIDSerializer.INSTANCE.decode(null, inputBuffer);
		EntityNature nature = EntityNatureSerializer.INSTANCE.decode(null, inputBuffer);
		Entity entity = Entity.EntitiesMap.get(uuid);
		WorldEntityComponent wec;
		if (entity == null) {
			wec = new WorldEntityComponent(new Position(new Coordinates(Chunk.WIDTH, Chunk.LENGTH), CardinalOrientation.SOUTH), nature);
			entity = new Entity(uuid).addComponent(WorldEntityComponent.ID, wec);
		} else
			wec = (WorldEntityComponent) entity.getComponent(WorldEntityComponent.ID);
		attachment = entity;
		wec.getPosition().get().coordinates = CoordinatesSerializer.INSTANCE.decode(null, inputBuffer);
		wec.getPosition().get().orientation = OrientationSerializer.INSTANCE.decode(null, inputBuffer);
		attachment.setName(StringSerializer.INSTANCE.decode(null, inputBuffer));
	}

	public static final Integer type = EntityUpdateMessage.class.getName().hashCode();
	public static final MessageAllocator<EntityUpdateMessage, Entity> allocator = (entity) -> new EntityUpdateMessage(
			entity);
}
