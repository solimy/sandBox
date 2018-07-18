package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.common.world.model.Chunk;

public enum CoordinatesSerializer implements Serializer<Coordinates> {
	INSTANCE;

	@Override
	public ByteBuffer encode(Coordinates e) {
		ByteBuffer encoded = allocateAndSetApplicability(e, 3 * Integer.BYTES);
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		System.out.println("encoded : " + e.toString());
		return encoded.putInt(e.getWorldX()).putInt(e.getWorldY()).putInt(e.getLayer());
	}

	@Override
	public Coordinates decode(Coordinates e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		Integer worldX = buffer.getInt();
		Integer worldY = buffer.getInt();
		Integer worldZ = buffer.getInt();
		if (e == null)
			e = new Coordinates(Chunk.WIDTH, Chunk.LENGTH);
		e = e.setWorldCoordinates(worldX, worldY, worldZ);
		return e;
	}
}
