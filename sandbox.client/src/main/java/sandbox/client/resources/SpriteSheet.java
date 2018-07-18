package sandbox.client.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import sandbox.engine.graphic.drawable.sprite.Sprite;
import sandbox.engine.math.Vector2D;

public class SpriteSheet {
	public enum ReadStyle {
		UP_LEFT_TO_DOWN_RIGHT;
	}

	private final Image sheet;

	public SpriteSheet(String url) {
		Image image = null;
		try {
			image = new Image(this.getClass().getResource(url).openStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.sheet = image;
		}
	}

	public Sprite extractSprite(Integer xPos, Integer yPos, Integer width, Integer height) {
		return new Sprite(new WritableImage(sheet.getPixelReader(), xPos, yPos, width, height));
	}
	
	public Sprite extractSprite() {
		return new Sprite(sheet);
	}

	public List<Sprite> extractAnimation(ReadStyle syle, Integer width, Integer height, Vector2D firstIndex,
			Vector2D lastIndex) {
		final List<Sprite> images = new ArrayList<>();
		final Double maxXIndex = sheet.getWidth() / width;
		final Double maxYIndex = sheet.getHeight() / height;
		final Vector2D last = lastIndex;
		switch (syle) {
		case UP_LEFT_TO_DOWN_RIGHT:
			for (Integer y = firstIndex.y; y <= last.y; ++y) {
				for (Integer x = firstIndex.x; x <= last.x; ++x) {
					images.add(extractSprite(x * width, y * height, width, height));
				}
			}
			break;
		default:
			break;
		}
		return images;
	}
}
