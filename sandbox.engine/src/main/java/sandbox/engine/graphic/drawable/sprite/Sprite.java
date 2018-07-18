package sandbox.engine.graphic.drawable.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class Sprite {
	public static final Sprite empty = new Sprite(new WritableImage(1, 1));

	private final Image original;
	private final Integer originalWidth;
	private final Integer originalHeight;
	private double xOrigin = 0D;
	private double yOrigin = 0D;
	private double x = 0D;
	private double y = 0D;
	private Double xScale = null;
	private Double yScale = null;
	private Integer fitWidth = null;
	private Integer fitHeight = null;

	public Sprite(Image image) {
		original = image;
		originalWidth = (int) image.getWidth();
		originalHeight = (int) image.getHeight();
		fitWidth = originalWidth;
		fitHeight = originalHeight;
	}

	public Sprite reset() {
		xScale = 100.0;
		yScale = 100.0;
		fitHeight = originalHeight;
		fitWidth = originalWidth;
		xOrigin = 0.0;
		yOrigin = 0.0;
		return this;
	}

	public Sprite setScale(Double xScale, Double yScale) {
		this.xScale = xScale;
		this.yScale = yScale;
		fitWidth = (int) (originalWidth * xScale);
		fitHeight = (int) (originalHeight * yScale);
		return this;
	}

	public Sprite setFit(Integer fitWidth, Integer fitHeight) {
		this.fitHeight = fitHeight;
		this.fitWidth = fitWidth;
		return this;
	}

	public Sprite setOrigin(double x, double y) {
		xOrigin = x;
		yOrigin = y;
		return this;
	}

	public Sprite setXY(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public void render(GraphicsContext context) {
		context.drawImage(original, x+xOrigin, y+yOrigin, fitWidth, fitHeight);
	}

	public Image getOriginal() {
		return original;
	}
}
