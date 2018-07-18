package sandbox.engine.network.message;

import java.nio.ByteBuffer;

public class Header {
	private Integer messageSize = null;
	private Integer messageType = null;
	
	public static Integer getSize() {
		return Integer.BYTES * 2;
	}
	
	public Integer getMessageSize() {
		return messageSize;
	}

	public Integer getMessageType() {
		return messageType;
	}
	
	public void read(ByteBuffer buffer) {
		this.messageSize = buffer.getInt();
		this.messageType = buffer.getInt();
	}
	
	public void reset() {
		this.messageSize = null;
		this.messageType = null;
	}
}
