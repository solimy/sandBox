package sandbox.common.protocol.messages.chunk;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.engine.logging.Logger;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class ChunkEntitiesGetMessage extends ProtocolMessage {
	public static final Integer TYPE = ChunkEntitiesGetMessage.class.getName().hashCode();
	public final Coordinates coordinates;
	
	public ChunkEntitiesGetMessage(Coordinates coordinates) {
		super(new RawMessage(TYPE, coordinates));
		this.coordinates = coordinates;
	}


	public ChunkEntitiesGetMessage(RawMessage rawMessage) {
		super(rawMessage);
		this.coordinates = (Coordinates) rawMessage.getWord(0);
	}


	public static void main(String[] args) {
		Coordinates coordinates = new Coordinates(10, 10).setWorldCoordinates(199, 5, 0);
		ChunkEntitiesGetMessage chunkEntitiesGetMessage = new ChunkEntitiesGetMessage(coordinates);
		Logger.INSTANCE.debug(chunkEntitiesGetMessage.coordinates);
		ByteBuffer byteBuffer = chunkEntitiesGetMessage.getRawMessage().getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer.compact();
		chunkEntitiesGetMessage = new ChunkEntitiesGetMessage(new RawMessage(header, byteBuffer));
		Logger.INSTANCE.debug(chunkEntitiesGetMessage.coordinates);
	}
}
