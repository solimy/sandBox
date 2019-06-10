package sandbox.engine.graphic;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sandbox.engine.Engine;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.drawable.sprite.Sprite;

public class GraphicApplication<E extends MainScript<E>> {

	private final JFrame frame = new JFrame();
	private final E mainScript;
	private Script<GraphicApplication<E>> onRenderScrit = (Script<GraphicApplication<E>>) Script.EMPTY;
	private final AtomicLong frameRateMillis = new AtomicLong((long) (1000 / 60));
	private final AtomicBoolean running = new AtomicBoolean(false);

	public GraphicApplication(E mainScript) {
		this.mainScript = mainScript;
	}

	public final void start() throws InterruptedException, InvocationTargetException {
		running.set(true);
		frame.setVisible(true);
		mainScript.execute(this);

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
				onRenderScrit.execute(this);
			});
			Engine.Clock.INSTANCE.updateMillis();
			nextFrame = ((long) Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastupdate);
			nextFrame = nextFrame < frameRateMillis.get() ? frameRateMillis.get() - nextFrame : 0;
		}
	}

	public final GraphicApplication<E> setTitle(String title) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> frame.setTitle(title));
		return this;
	}

	public final GraphicApplication<E> setFramesPerSecond(Long fps) {
		this.frameRateMillis.set(1000 / fps);
		return this;
	}

	public final GraphicApplication<E> setSize(int width, int height)
			throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> frame.setSize(width, height));
		return this;
	}

	public final GraphicApplication<E> setOnRenderScript(Script<GraphicApplication<E>> onRenderScript) {
		this.onRenderScrit = onRenderScript;
		return this;
	}

	public final GraphicApplication<E> setOnKeyPressedScript(Script<KeyEvent> onKeyPressedScript) {
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

	public final GraphicApplication<E> setOnKeyReleasedScript(Script<KeyEvent> onKeyReleasedScript) {
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

	public final GraphicApplication<E> setOnKeyTypedScript(Script<KeyEvent> onKeyTypedScript) {
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

	public final GraphicApplication<E> setOnResizeScript(Script<GraphicApplication<E>> onResizeScript) {
		return this;
	}

	public final E getMainScript() {
		return mainScript;
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