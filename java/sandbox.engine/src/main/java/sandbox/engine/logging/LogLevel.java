package sandbox.engine.logging;

enum LogLevel {
	DEBUG(1),
	INFO(2),
	WARNING(3),
	ERROR(4),
	CRITICAL(5),
	LOGGER(6);
	
	private final int crititicy;
	private LogLevel(int criticity) {
		this.crititicy = criticity;
	}
	
	public boolean includes(LogLevel other) {
		return other.crititicy >= this.crititicy;
	}
}
