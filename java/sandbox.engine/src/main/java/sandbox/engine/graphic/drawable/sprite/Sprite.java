package sandbox.engine.graphic.drawable.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import sandbox.engine.logging.Logger;

public class Sprite {
	private static final Image emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);	
	public static final Sprite empty = new Sprite(emptyImage);

	private final Image originalImage;
	private final AtomicReference<Image> scaledImageRef = new AtomicReference<Image>(null);

	private int x = 0;
	private int y = 0;
	
	private int width = 0;
	private int height = 0;
	private int scaledWidth = 0;
	private int scaledHeight = 0;
	private int xScale = 100;
	private int yScale = 100;
	private int pixelUnit = 1;

	private int xOrigin = 0;
	private int yOrigin = 0;
	private int xOffset = 0;
	private int yOffset = 0;

	public Sprite(Image image) {
		originalImage = image;
		setSize(image.getWidth(null) * 1000, image.getHeight(null) * 1000);
	}


	private Sprite updatePixelUnit(int pixelUnit) {
		if (this.pixelUnit == pixelUnit)
			return this;
		this.pixelUnit = pixelUnit;
		applyScale();
		applyOrigin();
		return this;
	}
	
	/**
	 * 
	 * Sets the scale and scales to this scale.<br/>
	 * 
	 * @param xScale in percent (from 0 to 100)
	 * @param yScale in percent (from 0 to 100)
	 * @return
	 */
	public Sprite setScale(int xScale, int yScale) {
		if (this.xScale == xScale && this.yScale == yScale)
			return this;
		this.xScale = xScale;
		this.yScale = yScale;
		applyScale();
		applyOrigin();
		return this;
	}

	private void applyScale() {
		scaledWidth = (width * xScale) / 100;
		scaledHeight = (height * yScale) / 100;
		Image image = emptyImage;
		try {
			image = originalImage.getScaledInstance(
					(scaledWidth * pixelUnit) / 1000,
					(scaledHeight * pixelUnit) / 1000,
					Image.SCALE_SMOOTH);
		} catch (Exception e) {
			Logger.INSTANCE.error("Sprite : failed to apply scale."
					+ "\n Exception caught : "
					+ e);
		}
		scaledImageRef.set(image);
	}

	/**
	 * 
	 * Warning: this is a little complex but crucial, read carefully.<br/>
	 * <br/>
	 * Here, we want to set the size of the image in milliPixelUnit.<br/>
	 * What is PixelUnit ? It is the generic size dimension of sprites, to scale with the display size.<br/>
	 * You have to define how many pixels is a PixelUnit, and your sprites will automatically scale.<br/>
	 * <br/>
	 * For exemple, if your sprite has a 1000 milliPixelUnit width and a 500 milliPixelUnit height:<br/>
	 * > for PixelUnit == 10 pixels:<br/>
	 * >> sprite width == 10 pixels<br/>
	 * >> sprite height == 5 pixels<br/>
	 * 
	 * @param width in milliPixelUnit
	 * @param height in milliPixelUnit
	 * @return this
	 */
	public Sprite setSize(int width, int height) {
		if (this.width == width && this.height == height)
			return this;
		this.width = width;
		this.height = height;
		applyScale();
		applyOrigin();
		return this;
	}

	/**
	 * 
	 * Sets the origin of the sprite, with a percentage of the width and height of the sprite.<br/>
	 * By default, the origin is at (0, 0), meaning left top of the sprite.<br/>
	 * 
	 * @param x in percent
	 * @param y in percent
	 * @return
	 */
	public Sprite setOrigin(int x, int y) {
		if (xOrigin == x && yOrigin == y)
			return this;
		xOrigin = x;
		yOrigin = y;
		applyOrigin();
		return this;
	}

	private void applyOrigin() {
		xOffset = (scaledImageRef.get().getWidth(null) * xOrigin) / 100;
		yOffset = (scaledImageRef.get().getHeight(null) * yOrigin) / 100;
	}

	/**
	 * 
	 * Sets the on display position of the sprite
	 * 
	 * @param x in pixels
	 * @param y in pixels
	 * @return
	 */
	public Sprite setXY(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public void render(Graphics context, int pixelUnit) {
		updatePixelUnit(pixelUnit);
		context.drawImage(scaledImageRef.get(), x+xOffset, y+yOffset, null);
	}
}
