package sandbox.engine.network.message;

import java.nio.ByteBuffer;

public class Header {
	public static final Short SIZE =Integer.BYTES * 3; 
	public final Integer messageSize;
	public final Integer messageType;
	public final Integer messageWordsCount;
	
	public Header(ByteBuffer buffer) {
		this.messageType = buffer.getInt();
		this.messageSize = buffer.getInt();
		this.messageWordsCount = buffer.getInt();
	}

	public Header(Integer messageType, Integer messageSize, Integer messageWordsCount) {
		this.messageType = messageType;
		this.messageSize = messageSize;
		this.messageWordsCount = messageWordsCount;
	}
}
