package sandbox.common.math.position;

import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;
import sandbox.engine.misc.Copyable;

public class Coordinates implements Copyable<Coordinates> {
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

	@Override
	public String toString() {
		return "Coordinates [worldX=" + worldX + ", worldY=" + worldY + ", chunkX=" + chunkX + ", chunkY=" + chunkY
				+ ", inChunkX=" + inChunkX + ", inChunkY=" + inChunkY + ", layer=" + layer + "]";
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
}
