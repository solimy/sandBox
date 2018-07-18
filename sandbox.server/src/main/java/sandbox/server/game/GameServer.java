package sandbox.server.game;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.math.position.Coordinates;
import sandbox.common.world.Constraints;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.Engine;
import sandbox.engine.game.Entity;
import sandbox.engine.math.Vector2D;
import sandbox.engine.network.Connection;
import sandbox.engine.network.message.Message;
import sandbox.server.game.components.PlayerComponent;
import sandbox.server.network.AsyncNIOServer;

public enum GameServer {
	INSTANCE;
	private final Map<Connection, Entity> players = new ConcurrentHashMap<>();
	private final Map<UUID, Entity> playersUUID = new ConcurrentHashMap<>();
	private final Map<UUID, Connection> connectionsUUID = new ConcurrentHashMap<>();
	private AsyncNIOServer server;

	private void updateRoutine() {
		try {
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		players.values().parallelStream().forEach(player -> {
			WorldEntityComponent wec = (WorldEntityComponent) player.getComponent(WorldEntityComponent.ID);
			if (wec != null) {
				Vector2D mod = new Vector2D();
				for (mod.y = -Constraints.VIEW_RANGE; mod.y <= Constraints.VIEW_RANGE; ++mod.y) {
					for (mod.x = -Constraints.VIEW_RANGE; mod.x <= Constraints.VIEW_RANGE; ++mod.x) {
						World.INSTANCE.getChunk(
								new Coordinates(wec.getPosition().get().coordinates).modChunkCoordinates(mod, null))
								.update(currentTimeMillis);
					}
				}
			}
		});
		} catch (Exception e) {
			System.err.println("GameServer::updateRoutine");
			e.printStackTrace();
		}
	}

	private void logRoutine() {
		System.out.println(
				"GameServer::main : Connections :\n" + GameServer.INSTANCE.players.entrySet().stream().map(entry -> {
					Connection key = entry.getKey();
					Entity value = entry.getValue();
					if (!key.isOpen()) {
						return "\t" + "connection closed" + " : " + value.getUUID() + " : " + value.getName();
					}
					return "\t" + key.remoteAdress + " : " + value.getUUID() + " : " + value.getName();
				}).collect(Collectors.joining("\n")));
	}

	public void broadcast(Coordinates coordinates, Integer chunkRange, Message<?, ?> message) {
		Vector2D mod = new Vector2D();
		for (mod.y = -chunkRange; mod.y <= chunkRange; ++mod.y) {
			for (mod.x = -chunkRange; mod.x <= chunkRange; ++mod.x) {
				Chunk chunk = World.INSTANCE.getNullChunk(new Coordinates(coordinates).modChunkCoordinates(mod, null));
				if (chunk != null) {
					chunk.dynamicEntities.forEach((u, e) -> {
						Connection connection = connectionsUUID.get(u);
						if (connection != null)
							connection.send(message);
					});
				}
			}
		}
	}

	public void broadcast(Coordinates coordinatesNO, Coordinates coordinatesSE, Message<?, ?> message) {
		Vector2D modX = new Vector2D(1, 0), modY = new Vector2D(0, -1);
		Coordinates cursor = new Coordinates(coordinatesNO);
		while (cursor.getChunkY() >= coordinatesSE.getChunkY()) {
			while (cursor.getChunkX() <= coordinatesSE.getChunkX()) {
				Chunk chunk = World.INSTANCE.getNullChunk(cursor);
				if (chunk != null) {
					chunk.dynamicEntities.forEach((u, e) -> {
						Connection connection = connectionsUUID.get(u);
						if (connection != null)
							connection.send(message);
					});
				}
				cursor.modChunkCoordinates(modX, null);
			}
			cursor.modChunkCoordinates(modY, null);
			cursor.setChunkX(coordinatesNO.getChunkX());

		}
	}

	public void newPlayer(Connection connection, Entity player) {
		players.put(connection, player);
		playersUUID.put(player.getUUID(), player);
		connectionsUUID.put(player.getUUID(), connection);
		player.addComponent(PlayerComponent.ID, new PlayerComponent());
	}

	public void deletePlayer(Entity player) {
		UUID uuid = player.getUUID();
		playersUUID.remove(uuid);
		Connection connection = connectionsUUID.remove(uuid);
		players.remove(connection);
	}

	public Entity getPlayer(Connection connection) {
		return players.get(connection);
	}

	public static void main(String[] args) {
		try {
			World.INSTANCE.entityManager.createChunkIfNotPresent(true);
			World.INSTANCE.entityManager.askStateManagerBeforeMove(true);
			Engine.Clock.INSTANCE.start();
			GameServer.INSTANCE.server = new AsyncNIOServer("localhost", 8983);
			GameServer.INSTANCE.server.start();
			Engine.INSTANCE.scheduleWithFixedDelay(GameServer.INSTANCE::updateRoutine, 0, Engine.INSTANCE.cadenceMillis,
					TimeUnit.MILLISECONDS);
			Engine.INSTANCE.scheduleWithFixedDelay(GameServer.INSTANCE::logRoutine, 0, 5000, TimeUnit.MILLISECONDS);
		} catch (IOException e) {
			System.err.println("GameServer::main : " + e.toString() + " - " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.err.println("GameServer::main : terminated");
		}
	}
}
