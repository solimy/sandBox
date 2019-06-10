package sandbox.engine.graphic;

import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sandbox.engine.Engine;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.drawable.sprite.Sprite;

public enum GraphicApplication {
	INSTANCE;

	private JFrame frame;
	private Script<?> onRenderScrit = Script.EMPTY;
	private final AtomicLong frameRateMillis = new AtomicLong((long) (1000 / 60));
	private final AtomicBoolean running = new AtomicBoolean(false);

	public final GraphicApplication init() {
		frame = new JFrame();
		frame.resize(800, 600);
		return this;
	}
	
	public final void start() throws InterruptedException, InvocationTargetException {
		running.set(true);
		frame.setVisible(true);

		Long nextFrame = 0L;
		while (running.get()) {
			if (nextFrame > 0) {
				Thread.sleep(nextFrame);
			}
			Engine.Clock.INSTANCE.updateMillis();
			Long lastupdate = Engine.Clock.INSTANCE.getCurrentTimeMillis();
			SwingUtilities.invokeAndWait(() -> {
				frame.getGraphics().clearRect(0, 0, frame.getWidth(), frame.getHeight());
			});
			SwingUtilities.invokeAndWait(() -> {
				onRenderScrit.execute(null);
			});
			Engine.Clock.INSTANCE.updateMillis();
			nextFrame = ((long) Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastupdate);
			nextFrame = nextFrame < frameRateMillis.get() ? frameRateMillis.get() - nextFrame : 0;
		}
	}

	public final GraphicApplication setTitle(String title) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> frame.setTitle(title));
		return this;
	}

	public final GraphicApplication setFramesPerSecond(Long fps) {
		this.frameRateMillis.set(1000 / fps);
		return this;
	}

	public final GraphicApplication setSize(int width, int height)
			throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> frame.setSize(width, height));
		return this;
	}

	public final GraphicApplication setOnRenderScript(Script<Void> onRenderScript) {
		this.onRenderScrit = onRenderScript;
		return this;
	}

	public final GraphicApplication setOnKeyPressedScript(Script<KeyEvent> onKeyPressedScript) {
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (onKeyPressedScript != null)
					onKeyPressedScript.execute(e);
			}
		});
		return this;
	}

	public final GraphicApplication setOnKeyReleasedScript(Script<KeyEvent> onKeyReleasedScript) {
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyPressed(e);
				if (onKeyReleasedScript != null)
					onKeyReleasedScript.execute(e);
			}
		});
		return this;
	}

	public final GraphicApplication setOnKeyTypedScript(Script<KeyEvent> onKeyTypedScript) {
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (onKeyTypedScript != null)
					onKeyTypedScript.execute(e);
			}
		});
		return this;
	}

	public final GraphicApplication setOnResizeScript(Script<Void> onResizeScript) {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				if (onResizeScript != null)
					onResizeScript.execute(null);
			}
		});
		return this;
	}

	public final void render(Sprite sprite) {
		Graphics2D g2D = (Graphics2D) frame.getGraphics();
		sprite.render(g2D);
	}

	@Deprecated
	public final JFrame getFrame() {
		return frame;
	}
}