package sandbox.common.protocol.messages.chunk;

import java.nio.ByteBuffer;

import sandbox.common.math.position.Coordinates;
import sandbox.engine.logging.Logger;
import sandbox.engine.network.message.Header;
import sandbox.engine.network.message.ProtocolMessage;
import sandbox.engine.network.message.RawMessage;

public class ChunkTerrainGetMessage extends ProtocolMessage {
	public static final Integer TYPE = ChunkTerrainGetMessage.class.getName().hashCode();
	public final Coordinates coordinates;
	
	public ChunkTerrainGetMessage(Coordinates coordinates) {
		super(new RawMessage(TYPE, coordinates));
		this.coordinates = coordinates;
	}
	
	public ChunkTerrainGetMessage(RawMessage rawMessage) {
		super(rawMessage);
		this.coordinates = (Coordinates) rawMessage.getWord(0);
	}


	public static void main(String[] args) {
		Coordinates coordinates = new Coordinates(10, 10).setWorldCoordinates(199, 5, 0);
		ChunkTerrainGetMessage chunkTerrainGetMessage = new ChunkTerrainGetMessage(coordinates);
		Logger.INSTANCE.debug(chunkTerrainGetMessage.coordinates);
		ByteBuffer byteBuffer = chunkTerrainGetMessage.getRawMessage().getAsByteBuffer();
		Header header = new Header(byteBuffer);
		byteBuffer.compact();
		chunkTerrainGetMessage = new ChunkTerrainGetMessage(new RawMessage(header, byteBuffer));
		Logger.INSTANCE.debug(chunkTerrainGetMessage.coordinates);
	}
}
