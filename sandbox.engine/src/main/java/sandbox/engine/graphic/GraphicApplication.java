package sandbox.engine.graphic;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;

import sandbox.engine.Engine;
import sandbox.engine.game.Script;

public class GraphicApplication<E extends MainScript<E>> {

	private final AtomicReference<JFrame> frameRef = new AtomicReference<JFrame>(null);
	private final E mainScript;
	private Script<E> onRenderScrit = (Script<E>) Script.EMPTY;
	private final AtomicLong frameRateMillis = new AtomicLong((long) (1000 / 60));
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public GraphicApplication(E mainScript) {
		this.mainScript = mainScript;
	}


	public final void start() throws Exception {
		running.set(true);
		frameRef.set(new JFrame());

		Long nextFrame = 0L;
		while (running.get()) {
			
			if (nextFrame > 0) {
				Thread.sleep(nextFrame);
			}
			Engine.Clock.INSTANCE.updateMillis();
			Long lastupdate = Engine.Clock.INSTANCE.getCurrentTimeMillis();
			onRenderScrit.execute(mainScript);
			Engine.Clock.INSTANCE.updateMillis();
			nextFrame = ((long) Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastupdate);
			nextFrame = nextFrame < frameRateMillis.get() ? frameRateMillis.get() - nextFrame : 0;
		}
	}

	public final GraphicApplication<E> setTitle(String title) {
		Optional.of(frameRef.get()).ifPresent(frame -> frame.setTitle(title));
		return this;
	}

	public final GraphicApplication<E> setFramesPerSecond(Long fps) {
		this.frameRateMillis.set(1000 / fps);
		return this;
	}

	public final GraphicApplication<E> setSize(int width, int height) {
		Optional.of(frameRef.get()).ifPresent(frame -> frame.setSize(width, height));
		return this;
	}
	
	public final GraphicApplication<E> setOnRenderScript(Script<E> onRenderScript) {
		this.onRenderScrit = onRenderScript;
		return this;
	}

	public final GraphicApplication<E> setOnKeyEventScript(Script<KeyEvent> onKeyPressedScript) {
		return this;
	}

	public final GraphicApplication<E> setOnResizeScript(Script<GraphicApplication> onResizeScript) {
		return this;
	}
}