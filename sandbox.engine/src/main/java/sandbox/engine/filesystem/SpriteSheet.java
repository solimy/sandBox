package sandbox.engine.filesystem;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import sandbox.engine.graphic.drawable.sprite.Sprite;
import sandbox.engine.math.Vector2D;

public class SpriteSheet {
	public enum ReadStyle {
		UP_LEFT_TO_DOWN_RIGHT;
	}

	private final BufferedImage sheet;

	public SpriteSheet(Class<?> klass, String ressourceUrl) {
		this(klass.getResourceAsStream(ressourceUrl));
	}
	
	public SpriteSheet(InputStream inStream) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.sheet = image;
		}
	}

	public Sprite extractSprite(Integer xPos, Integer yPos, Integer width, Integer height) {
		return new Sprite(sheet.getSubimage(xPos, yPos, width, height));
	}
	
	public Sprite extractSprite() {
		return new Sprite(sheet);
	}

	public List<Sprite> extractAnimation(ReadStyle style, Integer width, Integer height, Vector2D firstIndex,
			Vector2D lastIndex) {
		final List<Sprite> images = new ArrayList<>();
		final int maxXIndex = sheet.getWidth() / width;
		final int maxYIndex = sheet.getHeight() / height;
		final Vector2D last = lastIndex;
		switch (style) {
		case UP_LEFT_TO_DOWN_RIGHT:
			for (Integer y = firstIndex.y; y <= last.y && y <= maxYIndex; ++y) {
				for (Integer x = firstIndex.x; x <= last.x && x <= maxXIndex; ++x) {
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
