package sandbox.engine.graphic.drawable.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

public class Sprite {
	public static final Sprite empty = new Sprite(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY));

	private final Image originalImage;
	private final AtomicReference<Image> scaledImageRef = new AtomicReference<Image>(null);
	private int xOrigin = 0;
	private int yOrigin = 0;
	private int x = 0;
	private int y = 0;
	private int fitWidth = 0;
	private int fitHeight = 0;
	private int xScale = 100;
	private int yScale = 100;
	
	public Sprite(Image image) {
		originalImage = image;
		reset();
	}

	public Sprite reset() {
		xOrigin = 0;
		yOrigin = 0;
		scale(100, 100);
		return this;
	}

	public Sprite scale(int xScale, int yScale) {
		if (this.xScale == xScale && this.yScale == yScale)
			return this;
		this.xScale = xScale;
		this.yScale = yScale;
		fit(
			originalImage.getWidth(null) * xScale,
			originalImage.getHeight(null) * yScale
		);
		return this;
	}

	public Sprite fit(Integer fitWidth, Integer fitHeight) {
		if (this.fitWidth == fitWidth && this.fitHeight == fitHeight)
			return this;
		this.fitHeight = fitHeight;
		this.fitWidth = fitWidth;
		scaledImageRef.set(
				originalImage.getScaledInstance(
						this.fitWidth,
						this.fitHeight,
						Image.SCALE_SMOOTH)
		);
		return this;
	}

	public Sprite setOrigin(int x, int y) {
		xOrigin = x;
		yOrigin = y;
		return this;
	}

	public Sprite setXY(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public void render(Graphics context) {
		Image img = scaledImageRef.get();
		if (img != null)
			context.drawImage(img, x+xOrigin, y+yOrigin, null);
	}
}
