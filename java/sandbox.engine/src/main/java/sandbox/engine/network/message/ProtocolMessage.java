package sandbox.engine.network.message;

public abstract class ProtocolMessage {
	private final RawMessage rawMessage;
	
	protected ProtocolMessage(RawMessage rawMessage) {
		this.rawMessage = rawMessage;
	}
	
	public RawMessage getRawMessage() {
		return this.rawMessage;
	}
}
