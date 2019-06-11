package sandbox.engine.graphic;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import sandbox.engine.Engine;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.drawable.sprite.Sprite;

public enum GraphicApplication {
	INSTANCE;

	private JFrame window;
	private Canvas cameraDisplay;
	private Script<?> onRenderScrit = Script.EMPTY;
	private final AtomicLong frameRateMillis = new AtomicLong((long) (1000 / 60));
	private final AtomicBoolean running = new AtomicBoolean(false);
	private volatile Graphics context = null;

	public final GraphicApplication init(String title, int width, int height) {
        window = new JFrame();

        cameraDisplay = new Canvas();
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem startMenuItem = new JMenuItem("Pause");
        menuBar.add(fileMenu);
        fileMenu.add(startMenuItem);

        window.add(cameraDisplay);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setJMenuBar(menuBar);
        
        
		window.setTitle(title);
		window.setSize(width, height);
		return this;
	}

	public final void start() throws InterruptedException, InvocationTargetException {
		running.set(true);
		window.setVisible(true);

		cameraDisplay.setVisible(true);
		cameraDisplay.createBufferStrategy(2);
		BufferStrategy strategy = cameraDisplay.getBufferStrategy();

		Long nextFrame = 0L;
		while (running.get()) {
			do {
				do {
					context = strategy.getDrawGraphics();
					if (nextFrame > 0) {
						Thread.sleep(nextFrame);
					}
					Engine.Clock.INSTANCE.updateMillis();
					Long lastupdate = Engine.Clock.INSTANCE.getCurrentTimeMillis();
					context.clearRect(0, 0, cameraDisplay.getWidth(), cameraDisplay.getHeight());
					onRenderScrit.execute(null);
					Engine.Clock.INSTANCE.updateMillis();
					nextFrame = ((long) Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastupdate);
					nextFrame = nextFrame < frameRateMillis.get() ? frameRateMillis.get() - nextFrame : 0;
					context.dispose();
				} while (strategy.contentsRestored());
				strategy.show();
			} while (strategy.contentsLost());
		}

		window.setVisible(false);
		window.dispose();
	}

	public final GraphicApplication setTitle(String title) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> window.setTitle(title));
		return this;
	}

	public final GraphicApplication setFramesPerSecond(Long fps) {
		this.frameRateMillis.set(1000 / fps);
		return this;
	}

	public final GraphicApplication setSize(int width, int height)
			throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> window.setSize(width, height));
		return this;
	}

	public final GraphicApplication setOnRenderScript(Script<Void> onRenderScript) {
		this.onRenderScrit = onRenderScript;
		return this;
	}

	public final GraphicApplication setOnKeyPressedScript(Script<KeyEvent> onKeyPressedScript) {
		cameraDisplay.addKeyListener(new KeyAdapter() {
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
		cameraDisplay.addKeyListener(new KeyAdapter() {
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
		cameraDisplay.addKeyListener(new KeyAdapter() {
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
		cameraDisplay.addComponentListener(new ComponentAdapter() {
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
		sprite.render(context);
	}
	
	public int getWidth() {
		return window.getWidth();
	}
	
	public int getHeight() {
		return window.getHeight();
	}
}