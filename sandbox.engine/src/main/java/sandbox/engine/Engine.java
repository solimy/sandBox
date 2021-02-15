package sandbox.engine;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import sandbox.engine.logging.Logger;
import sandbox.engine.settings.Settings;

public enum Engine {
	INSTANCE;
	
	public enum Clock {
		INSTANCE;

		ScheduledFuture<?> execution = null;
		Long lastTimeUpdateMillis;
		AtomicLong currentTimeMillis = new AtomicLong();
		Double speedFactor = 1.0;

		private Clock() {
			lastTimeUpdateMillis = System.currentTimeMillis();
			currentTimeMillis.set(lastTimeUpdateMillis);
		}
		
		public void updateMillis() {
			Long currentTimeMillis = System.currentTimeMillis();
			this.currentTimeMillis.addAndGet((long) ((currentTimeMillis - lastTimeUpdateMillis)*speedFactor));
			lastTimeUpdateMillis = currentTimeMillis; 
		}
		
		public void setSpeedFactor(Double speedFactor) {
			this.speedFactor = speedFactor;
		}

		public void start() {
			lastTimeUpdateMillis = System.currentTimeMillis();
			currentTimeMillis.set(lastTimeUpdateMillis);
			Clock.INSTANCE.execution = Engine.INSTANCE.scheduler.scheduleAtFixedRate(() -> Clock.INSTANCE.updateMillis(), 0,
					Engine.INSTANCE.cadenceMillis, TimeUnit.MILLISECONDS);
		}

		public void stop() {
			execution.cancel(false);
		}
		
		public void pause() {
			execution.cancel(false);
		}
		
		public void resume() {
			lastTimeUpdateMillis = System.currentTimeMillis();
			Clock.INSTANCE.execution = Engine.INSTANCE.scheduler.scheduleAtFixedRate(() -> Clock.INSTANCE.updateMillis(), 0,
					Engine.INSTANCE.cadenceMillis, TimeUnit.MILLISECONDS);
		}
		
		public Long getCurrentTimeMillis() {
			return currentTimeMillis.get();
		}
	}


	private final ScheduledExecutorService scheduler;

	public final Integer numberOfThreads;
	public final Integer cadenceMillis;

	private Engine() {
		Integer numberOfThreads = 10;
		Integer cadenceMillis = 100;

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			Settings settings = new Settings();
			parser.parse(ClassLoader.getSystemResourceAsStream("engine.settings.xml"), settings);
			numberOfThreads = Optional
				.ofNullable(settings.get("numberOfThreads"))
				.map(setting -> Integer.parseInt(setting))
				.orElse(10);
			cadenceMillis = Optional
				.ofNullable(settings.get("cadenceMillis"))
				.map(setting -> Integer.parseInt(setting))
				.orElse(100);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		this.numberOfThreads = numberOfThreads;
		this.cadenceMillis = cadenceMillis;
		Logger.INSTANCE.debug("Engine settings : {");
		Logger.INSTANCE.debug("    number of threads : " + this.numberOfThreads);
		Logger.INSTANCE.debug("    cadence (milliseconds) : " + this.cadenceMillis);
		Logger.INSTANCE.debug("}");
		scheduler = Executors.newScheduledThreadPool(this.numberOfThreads);
	}

	public void scheduleWithFixedDelay(Runnable command, int initialDelay, Integer delay, TimeUnit unit) {
		scheduler.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	public void schedule(Runnable command) {
		scheduler.execute(command);
	}
}
