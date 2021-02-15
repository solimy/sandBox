package sandbox.engine.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

public enum Logger {
	INSTANCE;

	private final LogLevel loglevel;
	private final PrintWriter logfile;
	
	private Logger() {
		final String loglevel_env = Optional.ofNullable(System.getenv("LOGLEVEL")).orElse("INFO");
		final String logfile_env = Optional.ofNullable(System.getenv("LOGFILE")).orElse("sandbox.default.log");
		LogLevel loglevel = null; 
		switch (loglevel_env) {
		case "DEBUG":
			loglevel = LogLevel.DEBUG;
			break;
		case "INFO":
			loglevel = LogLevel.INFO;
			break;
		case "WARNING":
			loglevel = LogLevel.WARNING;
			break;
		case "ERROR":
			loglevel = LogLevel.ERROR;
			break;
		case "CRITICAL":
			loglevel = LogLevel.CRITICAL;
			break;
		default:
			loglevel = LogLevel.INFO;
		}
		this.loglevel = loglevel;
		PrintWriter printer = null;
		try {
			printer = new PrintWriter(new FileWriter(new File(logfile_env), true));
		} catch (Exception e) {
			this._log_no_file(LogLevel.CRITICAL, "Could not open logfile \"" + logfile_env + "\"");
			System.exit(1);
		} finally {
			this.logfile = printer;
		}
		this.log(LogLevel.LOGGER, "LOGLEVEL=" + loglevel.name());
	}
	
	private String format(LogLevel loglevel, Object message) {
		return String.format(
			"%1$-30s %2$-10s %3$-1s",
			LocalDateTime.now(Clock.systemUTC()),
			loglevel.name(),
			message != null ? message.toString() : null
		);
	}

	private void _log_no_file(LogLevel loglevel, Object message) {
		if (this.loglevel.includes(loglevel)) {
			synchronized (this) {
				System.out.println(format(loglevel, message));
			}
		}
	}

	private void log(LogLevel loglevel, Object message) {
		if (this.loglevel.includes(loglevel)) {
			synchronized (this) {
				String record = format(loglevel, message);
				System.out.println(record);
				this.logfile.append(record).append("\n");
				this.logfile.flush();
			}
		}
	}
	
	public void debug(Object message) {
		log(LogLevel.DEBUG, message);
	}
	
	public void info(Object message) {
		log(LogLevel.INFO, message);
	}
	
	public void warn(Object message) {
		log(LogLevel.WARNING, message);
	}
	
	public void error(Object message) {
		log(LogLevel.ERROR, message);
	}
	
	public void critical(Object message) {
		log(LogLevel.CRITICAL, message);
	}
	
	public static void main(String[] args) {
		Logger.INSTANCE.debug("does it work ?");
		Logger.INSTANCE.info("does it work ?");
		Logger.INSTANCE.warn("does it work ?");
		Logger.INSTANCE.error("does it work ?");
		Logger.INSTANCE.critical("does it work ?");
		Logger.INSTANCE.critical(null);
	}
}
