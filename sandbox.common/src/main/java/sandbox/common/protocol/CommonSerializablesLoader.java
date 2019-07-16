package sandbox.common.protocol;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Damage;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.world.enums.EntityNature;
import sandbox.common.world.model.Chunk;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.network.serialization.SerializableRegistryService;

public final class CommonSerializablesLoader {
	public static void load() {
		SerializableRegistryService.INSTANCE.register(EntityNature.TYPE, EntityNature.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(CardinalOrientation.TYPE, CardinalOrientation.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(Coordinates.TYPE, Coordinates.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(Position.TYPE, Position.DESERIALIZER);		
		SerializableRegistryService.INSTANCE.register(WorldEntityComponent.TYPE, WorldEntityComponent.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(Chunk.TYPE, Chunk.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(Damage.TYPE, Damage.DESERIALIZER);
		SerializableRegistryService.INSTANCE.register(Move.TYPE, Move.DESERIALIZER);
	}
}
