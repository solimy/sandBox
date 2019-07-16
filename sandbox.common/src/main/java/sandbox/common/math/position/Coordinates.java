package sandbox.common.math.position;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import sandbox.engine.logging.Logger;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;
import sandbox.engine.misc.Copyable;
import sandbox.engine.misc.UUID;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.RawMessage;
import sandbox.engine.network.serialization.Deserializer;
import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.SerializableRegistryService;

public class Coordinates implements Copyable<Coordinates>, Serializable {
	public static final int TYPE = Coordinates.class.getName().hashCode();
	
	public static final Deserializer DESERIALIZER = buffer -> {
		Integer subLayerLength = buffer.getInt();
		Integer subLayerWidth = buffer.getInt();
		Integer worldX = buffer.getInt();
		Integer worldY = buffer.getInt();
		Integer layer = buffer.getInt();
		return new Coordinates(subLayerWidth, subLayerLength).setWorldCoordinates(worldX, worldY, layer);
	};
	
	private Integer subLayerWidth;
	private Integer subLayerLength;
	private Integer worldX = 0;
	private Integer worldY = 0;
	private Integer chunkX = 0;
	private Integer chunkY = 0;
	private Integer inChunkX = 0;
	private Integer inChunkY = 0;
	private Integer layer = 0;

	public Coordinates(Integer subLayerWidth, Integer subLayerLength) {
		this.subLayerWidth = subLayerWidth;
		this.subLayerLength = subLayerLength;

	}

	public Coordinates(Coordinates coordinates) {
		this.subLayerWidth = coordinates.subLayerWidth;
		this.subLayerLength = coordinates.subLayerLength;
		this.worldX = coordinates.worldX;
		this.worldY = coordinates.worldY;
		this.chunkX = coordinates.chunkX;
		this.chunkY = coordinates.chunkY;
		this.inChunkX = coordinates.inChunkX;
		this.inChunkY = coordinates.inChunkY;
		this.layer = coordinates.layer;
	}

	@Override
	public Coordinates copy(Coordinates coordinates) {
		this.subLayerWidth = coordinates.subLayerWidth;
		this.subLayerLength = coordinates.subLayerLength;
		this.worldX = coordinates.worldX;
		this.worldY = coordinates.worldY;
		this.chunkX = coordinates.chunkX;
		this.chunkY = coordinates.chunkY;
		this.inChunkX = coordinates.inChunkX;
		this.inChunkY = coordinates.inChunkY;
		this.layer = coordinates.layer;
		return this;
	}

	public Coordinates setWorldCoordinates(Integer worldX, Integer worldY, Integer layer) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.layer = layer;
		this.chunkX = (worldX < 0 ? worldX - subLayerWidth + 1 : worldX) / subLayerWidth;
		this.chunkY = (worldY < 0 ? worldY - subLayerLength + 1 : worldY) / subLayerLength;
		this.inChunkX = (worldX < 0 ? subLayerWidth + (worldX % subLayerWidth) : worldX) % subLayerWidth;
		this.inChunkY = (worldY < 0 ? subLayerLength + (worldY % subLayerLength) : worldY) % subLayerLength;
		return this;
	}

	public Coordinates modWorldCoordinates(Vector2D modifier) {
		this.worldX += modifier.x;
		this.worldY += modifier.y;
		this.chunkX = (worldX < 0 ? worldX - subLayerWidth + 1 : worldX) / subLayerWidth;
		this.chunkY = (worldY < 0 ? worldY - subLayerLength + 1 : worldY) / subLayerLength;
		this.inChunkX = (worldX < 0 ? subLayerWidth + (worldX % subLayerWidth) : worldX) % subLayerWidth;
		this.inChunkY = (worldY < 0 ? subLayerLength + (worldY % subLayerLength) : worldY) % subLayerLength;
		return this;
	}

	public Coordinates modChunkCoordinates(Vector2D modifierChunk, Vector2D modifierInChunk) {
		if (modifierChunk != null) {
			this.chunkX += modifierChunk.x;
			this.chunkY += modifierChunk.y;
		}
		if (modifierInChunk != null) {
			this.inChunkX += modifierInChunk.x;
			this.inChunkY += modifierInChunk.y;
		}
		this.worldX = ((chunkX) * subLayerWidth) + inChunkX;
		this.worldY = ((chunkY) * subLayerLength) + inChunkY;
		return this;
	}

	public Coordinates setChunkCoordinates(Integer chunkX, Integer chunkY, Integer inChunkX, Integer inChunkY,
			Integer layer) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.inChunkX = inChunkX;
		this.inChunkY = inChunkY;
		this.worldX = ((chunkX) * subLayerWidth) + inChunkX;
		this.worldY = ((chunkY) * subLayerLength) + inChunkY;
		this.layer = layer;
		return this;
	}

	public Integer getWorldX() {
		return worldX;
	}

	public Coordinates setWorldX(Integer worldX) {
		this.worldX = worldX;
		this.chunkX = (worldX < 0 ? worldX - subLayerWidth + 1 : worldX) / subLayerWidth;
		this.inChunkX = (worldX < 0 ? subLayerWidth + (worldX % subLayerWidth) : worldX) % subLayerWidth;
		return this;
	}

	public Integer getWorldY() {
		return worldY;
	}

	public Coordinates setWorldY(Integer worldY) {
		this.worldY = worldY;
		this.chunkY = (worldY < 0 ? worldY - subLayerLength + 1 : worldY) / subLayerLength;
		this.inChunkY = (worldY < 0 ? subLayerLength + (worldY % subLayerLength) : worldY) % subLayerLength;
		return this;
	}

	public Integer getChunkX() {
		return chunkX;
	}

	public Coordinates setChunkX(Integer chunkX) {
		this.chunkX = chunkX;
		this.worldX = ((chunkX) * subLayerWidth) + inChunkX;
		return this;
	}

	public Integer getChunkY() {
		return chunkY;
	}

	public Coordinates setChunkY(Integer chunkY) {
		this.chunkY = chunkY;
		this.worldY = ((chunkY) * subLayerLength) + inChunkY;
		return this;
	}

	public Integer getInChunkX() {
		return inChunkX;
	}

	public Coordinates setInChunkX(Integer inChunkX) {
		this.inChunkX = inChunkX;
		this.worldX = ((chunkX) * subLayerWidth) + inChunkX;
		return this;
	}

	public Integer getInChunkY() {
		return inChunkY;
	}

	public Coordinates setInChunkY(Integer inChunkY) {
		this.inChunkY = inChunkY;
		this.worldY = ((chunkY) * subLayerLength) + inChunkY;
		return this;
	}

	public Integer getLayer() {
		return layer;
	}

	public Coordinates setLayer(Integer layer) {
		this.layer = layer;
		return this;
	}

	public Boolean isInChunkRange(Coordinates other, Integer rangeLimit) {
		if (other.chunkX >= chunkX - rangeLimit && 
				other.chunkX <= chunkX + rangeLimit && 
				other.chunkY >= chunkY - rangeLimit && 
				other.chunkY <= chunkY + rangeLimit) { 
			return true;
		}
		return false;
	}
	
	public Coordinates worldTranslation(CardinalOrientation orientation, Integer amount) {
		switch (orientation) {
		case NORTH:
			modWorldCoordinates(new Vector2D(0, amount));
			break;
		case EAST:
			modWorldCoordinates(new Vector2D(amount, 0));
			break;
		case SOUTH:
			modWorldCoordinates(new Vector2D(0, -amount));
			break;
		case WEST:
			modWorldCoordinates(new Vector2D(-amount, 0));
			break;
		default:
			break;
		}
		return this;
	}
	
	public Coordinates chunkTranslation(CardinalOrientation orientation, Integer amount) {
		switch (orientation) {
		case NORTH:
			modChunkCoordinates(new Vector2D(0, amount), null);
			break;
		case EAST:
			modChunkCoordinates(new Vector2D(amount, 0), null);
			break;
		case SOUTH:
			modChunkCoordinates(new Vector2D(0, -amount), null);
			break;
		case WEST:
			modChunkCoordinates(new Vector2D(-amount, 0), null);
			break;
		default:
			break;
		}
		return this;
	}

	@Override
	public Coordinates copy() {
		return new Coordinates(this);
	}
	
	@Override
	public String toString() {
		return "Coordinates [worldX=" + worldX + ", worldY=" + worldY + ", chunkX=" + chunkX + ", chunkY=" + chunkY
				+ ", inChunkX=" + inChunkX + ", inChunkY=" + inChunkY + ", layer=" + layer + "]";
	}

	@Override
	public ByteBuffer encodePayload() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 5);
		buffer.putInt(subLayerLength);
		buffer.putInt(subLayerWidth);
		buffer.putInt(worldX);
		buffer.putInt(worldY);
		buffer.putInt(layer);
		return buffer;
	}

	@Override
	public Integer getType() {
		return TYPE;
	}
	

	public static void main(String[] args) {
		List<Coordinates> coordinates = Arrays.asList(
				new Coordinates(10, 10).setWorldCoordinates(1, 87, 8),
				new Coordinates(5, 5).setWorldCoordinates(100, 234, 0)
		);
		coordinates.forEach(Logger.INSTANCE::debug);
		RawMessage rawMessage = new RawMessage(0, coordinates.toArray(new Serializable[coordinates.size()]));
		ByteBuffer byteBuffer = rawMessage.getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer = byteBuffer.compact();
		rawMessage = new RawMessage(header, byteBuffer);
		for (int i = 0; i < coordinates.size(); ++i) {
			Logger.INSTANCE.debug(((Coordinates) rawMessage.getWord(i)));
		}
	}
}
