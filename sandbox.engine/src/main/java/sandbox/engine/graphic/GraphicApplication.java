package sandbox.engine.graphic;

import java.util.concurrent.atomic.AtomicLong;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sandbox.engine.Engine;
import sandbox.engine.game.Script;

public class GraphicApplication<E extends MainScript<E>> extends Application {

	private static MainScript<? extends MainScript<?>> mainScriptHelper;
	private E mainScript;
	private Script<E> onUpdateScrit = (Script<E>) Script.EMPTY;
	private Script<E> onRenderScrit = (Script<E>) Script.EMPTY;

	private ChangeListener<? super Number> onResizeListeneer = (obs, ov, nv) -> {
	};

	public static final <E extends MainScript<E>> void lauchGraphicApplication(E mainScript) {
		GraphicApplication.mainScriptHelper = mainScript;
		Application.launch(new String[0]);
	}

	private Stage stage;
	private static Group root;
	private static Scene gameScene;
	private static Canvas canvas;
	private AtomicLong frameRateMillis = new AtomicLong((long) (1000 / 60));

	@Override
	public final void start(Stage primaryStage) throws Exception {
		mainScript = (E) mainScriptHelper;
		stage = primaryStage;
		root = new Group();
		canvas = new Canvas(stage.getWidth(), stage.getHeight());
		gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		gameScene = new Scene(root);
		stage.setScene(gameScene);
		stage.setOnCloseRequest((event) -> System.exit(1));
		stage.widthProperty().addListener(onResizeListeneer);
		stage.heightProperty().addListener(onResizeListeneer);
		mainScript.execute(this);

		stage.show();
		new AnimationTimer() {

			Long nextFrame = 0L;

			@Override
			public void handle(long now) {
				try {
					if (nextFrame > 0) {
						Thread.sleep(nextFrame);
					}
					Engine.Clock.INSTANCE.updateMillis();
					Long lastupdate = Engine.Clock.INSTANCE.getCurrentTimeMillis();
					onUpdateScrit.execute(mainScript);
					onRenderScrit.execute(mainScript);
					Engine.Clock.INSTANCE.updateMillis();
					nextFrame = ((long) Engine.Clock.INSTANCE.getCurrentTimeMillis() - lastupdate);
					nextFrame = nextFrame < frameRateMillis.get() ? frameRateMillis.get() - nextFrame : 0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public final GraphicApplication setTitle(String title) {
		stage.setTitle(title);
		return this;
	}

	public final GraphicApplication setFramesPerSecond(Long fps) {
		this.frameRateMillis.set(1000 / fps);
		return this;
	}

	public final GraphicApplication setOnUpdateScript(Script<E> onUpdateScript) {
		this.onUpdateScrit = onUpdateScript;
		return this;
	}

	public final GraphicApplication setOnRenderScript(Script<E> onRenderScript) {
		this.onRenderScrit = onRenderScript;
		return this;
	}

	public final GraphicApplication setOnKeyPressedScript(Script<KeyEvent> onKeyPressedScript) {
		gameScene.setOnKeyPressed((event) -> onKeyPressedScript.execute(event));
		return this;
	}

	public final GraphicApplication setOnKeyTypedScript(Script<KeyEvent> onKeyPressedScript) {
		gameScene.setOnKeyTyped((event) -> onKeyPressedScript.execute(event));
		return this;
	}

	public final GraphicApplication setOnKeyReleasedScript(Script<KeyEvent> onKeyPressedScript) {
		gameScene.setOnKeyReleased((event) -> onKeyPressedScript.execute(event));
		return this;
	}

	public final GraphicApplication setOnResizeScript(Script<GraphicApplication> onResizeScript) {
		stage.widthProperty().removeListener(onResizeListeneer);
		stage.heightProperty().removeListener(onResizeListeneer);
		onResizeListeneer = (obs, ov, nv) -> {
			canvas.setWidth(stage.getWidth());
			canvas.setHeight(stage.getHeight());
			onResizeScript.execute(this);
		};
		stage.widthProperty().addListener(onResizeListeneer);
		stage.heightProperty().addListener(onResizeListeneer);
		return this;
	}

	private static GraphicsContext gc; 
	public static GraphicsContext getGraphicsContext() {
		return gc;
	}
	
	public static Group getSceneRoot() {
		return root;
	}
}