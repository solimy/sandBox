package sandbox.engine.network.message;

import java.nio.ByteBuffer;

import sandbox.engine.network.serialization.Serializable;
import sandbox.engine.network.serialization.Serializer;

public class RawMessage {
	public static Integer MAX_SIZE = 4096;
	private final Header header;
	private final Serializable[] serializables;
	private final ByteBuffer payload;
	
	public RawMessage(Integer messageType, Serializable ... serializables) {
		this.serializables = serializables;
		ByteBuffer payload = ByteBuffer.allocate(0);
		if (serializables != null) {			
			for (Serializable serializable : serializables) {
				ByteBuffer serialized = Serializer.serialize(serializable);
				int newCapacity = payload.capacity() + serialized.capacity();
				ByteBuffer newPayload = ByteBuffer.allocate(newCapacity);
				newPayload.put(payload);
				newPayload.put(serialized);
				payload = newPayload;
				payload.rewind();
			}
		}
		this.payload = payload;
		this.header = new Header(messageType, this.payload.capacity(), serializables != null ? serializables.length : 0);
	}
	
	public RawMessage(Header header, ByteBuffer payload) {
		payload.rewind();
		this.header = header;
		this.payload = payload;
		this.serializables = new Serializable[header.messageWordsCount];
		for (int i = 0; i < header.messageWordsCount; ++i) {
			this.serializables[i] = Serializer.deserializeNext(this.payload);
		}
		this.payload.rewind();
	}
	
	public ByteBuffer getAsByteBuffer() {
		payload.rewind();
		ByteBuffer outputBuffer = ByteBuffer.allocate(Header.SIZE + payload.capacity());
		outputBuffer.putInt(header.messageType);
		outputBuffer.putInt(header.messageSize);
		outputBuffer.putInt(header.messageWordsCount);
		outputBuffer.put(payload);
		outputBuffer.rewind();
		return outputBuffer;
	}
	
	public Serializable getWord(int wordIndex) {
		return serializables[wordIndex];
	}
	
	public Header getHeader() {
		return header;
	}
}
