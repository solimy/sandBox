package sandbox.common.misc.serializer;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.common.world.enums.BiomeType;
import sandbox.common.world.enums.TerrainType;
import sandbox.common.world.model.Cell;
import sandbox.common.world.model.Chunk;

public enum ChunkTerrainSerializer implements Serializer<Chunk> {
	INSTAnCE;

	@Override
	public ByteBuffer encode(Chunk e) {
		ByteBuffer encoded = allocateAndSetApplicability(e,
				Integer.BYTES * 3 + Chunk.LENGTH * Chunk.WIDTH * Integer.BYTES * 5);
		if (encoded.capacity() == Byte.BYTES)
			return encoded;
		encoded.putInt(e.coordinates.getChunkX());
		encoded.putInt(e.coordinates.getChunkY());
		encoded.putInt(e.coordinates.getLayer());
		for (int i = 0; i < Chunk.LENGTH; i++) {
			for (int j = 0; j < Chunk.WIDTH; j++) {
				Cell cell = e.cells[i][j];
				encoded.putInt(cell.getBiomeType().ordinal());
				encoded.putInt(cell.getTerrainType().ordinal());
				Coordinates coordinates = cell.getCoordinates();
				encoded.putInt(coordinates.getWorldX());
				encoded.putInt(coordinates.getWorldY());
				encoded.putInt(coordinates.getLayer());
			}
		}
		return encoded;
	}

	@Override
	public Chunk decode(Chunk e, ByteBuffer buffer) {
		if (!isApplicable(buffer))
			return e;
		if (e == null) e = new Chunk();
		e.coordinates.setChunkCoordinates(buffer.getInt(), buffer.getInt(), 0, 0, buffer.getInt());
		for (int i = 0; i < Chunk.LENGTH; i++) {
			for (int j = 0; j < Chunk.WIDTH; j++) {
				BiomeType biomeType = BiomeType.getByOridinal(buffer.getInt());
				TerrainType terrainType = TerrainType.getByOrdinal(buffer.getInt());
				Coordinates coordinates = new Coordinates(Chunk.WIDTH, Chunk.LENGTH)
						.setWorldCoordinates(buffer.getInt(), buffer.getInt(), buffer.getInt());
				e.cells[i][j] = new Cell(coordinates, biomeType, terrainType);
			}
		}
		e.isGenerated = true;
		return e;
	}

}
