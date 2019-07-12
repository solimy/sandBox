package sandbox.client.game.render;

import java.lang.ref.WeakReference;

import sandbox.client.ClientScript;
import sandbox.client.game.utils.MovementSmoother;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.Messages;
import sandbox.common.world.Constraints;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.common.world.model.Cell;
import sandbox.common.world.model.Chunk;
import sandbox.common.world.model.World;
import sandbox.engine.Engine;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.game.Script;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;

public enum CameraScript implements Script<Void>, Component {
	INSTANCE;

	Integer height = 0;
	Integer width = 0;
	Position position = new Position(new Coordinates(Chunk.WIDTH, Chunk.LENGTH), CardinalOrientation.ANY);
	Integer pixelUnitRatio = 11; // number of visible cells
	Integer pixelUnit = 1; // size of a cell
	Integer puHeight = 0;
	Integer puWidth = 0;
	final MovementSmoother movementSmoother = new MovementSmoother(new WeakReference<Position>(position));
	boolean isRunning = false;
	private ScreenCoordinates screenCoordinates = null;

	public class ScreenCoordinates {
		public Integer x = null;
		public Integer y = null;

		public ScreenCoordinates(Coordinates coordinates) {
			x = (coordinates.getWorldX() - CameraScript.INSTANCE.position.coordinates.getWorldX()) * pixelUnit;
			y = height - pixelUnit
					- ((coordinates.getWorldY() - CameraScript.INSTANCE.position.coordinates.getWorldY()) * pixelUnit);
		}
	}

	public void move(Coordinates coordinates) {
		position.coordinates.copy(coordinates);
	}

	public void center(Coordinates coordinates) {
		position.coordinates.copy(coordinates).modWorldCoordinates(new Vector2D(-puWidth / 2, -puHeight / 2));
		System.out.println("Centered to:\n" + position.coordinates.toString());
	}

	public void move(CardinalOrientation move) {
		System.out.println("moving the camera");
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		switch (move) {
		case NORTH:
			position.coordinates.setWorldY(position.coordinates.getWorldY() + 1);
			position.orientation = CardinalOrientation.NORTH;
			break;
		case SOUTH:
			position.coordinates.setWorldY(position.coordinates.getWorldY() - 1);
			position.orientation = CardinalOrientation.SOUTH;
			break;
		case WEST:
			position.coordinates.setWorldX(position.coordinates.getWorldX() - 1);
			position.orientation = CardinalOrientation.WEST;
			break;
		case EAST:
			position.coordinates.setWorldX(position.coordinates.getWorldX() + 1);
			position.orientation = CardinalOrientation.EAST;
			break;
		default:
			break;
		}
		movementSmoother.move(move, pixelUnit, currentTimeMillis, EntityState.WALK.getMaximumDuration());
	}

	public void watch(World world) {
		// this.world = world;
	}

	@Override
	public void execute(Void unused) {
		Engine.Clock.INSTANCE.updateMillis();
		Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		Integer camXS = movementSmoother.xSmoother.get(currentTimeMillis);
		Integer camYS = movementSmoother.ySmoother.get(currentTimeMillis);
		Vector2D mod = new Vector2D();
		// draw décor
		for (mod.y = -2; mod.y < puHeight + 2; ++mod.y) {
			for (mod.x = -2; mod.x < puWidth + 2; ++mod.x) {
				Coordinates renderCoordinates = new Coordinates(position.coordinates).modWorldCoordinates(mod);
				Cell cell = World.INSTANCE.getNullCell(renderCoordinates);
				if (cell == null) {
					continue;
				}
				screenCoordinates = new ScreenCoordinates(renderCoordinates);
				screenCoordinates.x += camXS;
				screenCoordinates.y += camYS;
				GraphicApplication.INSTANCE.render(TerrainRenderer.getSprite(cell.getTerrainType()).fit(pixelUnit, pixelUnit)
						.setXY(screenCoordinates.x, screenCoordinates.y));
			}
		}
		// draw entities
		for (mod.y = -2; mod.y < puHeight + 2; ++mod.y) {
			for (mod.x = -2; mod.x < puWidth + 2; ++mod.x) {
				Coordinates renderCoordinates = new Coordinates(position.coordinates).modWorldCoordinates(mod);
				Cell cell = World.INSTANCE.getNullCell(renderCoordinates);
				if (cell == null) {
					continue;
				}
				screenCoordinates = new ScreenCoordinates(renderCoordinates);
				screenCoordinates.x += camXS;
				screenCoordinates.y += camYS;
				cell.getDynamicEntities().values().forEach(Entity::render);
			}
		}
	}

	public void updateSize(int width, int height) {
		this.height = height;
		this.width = width;
		pixelUnit = height > width ? width / pixelUnitRatio : height / pixelUnitRatio;
		pixelUnit = pixelUnit == 0 ? 1 : pixelUnit;
		puHeight = height / pixelUnit;
		puWidth = width / pixelUnit;
		System.out.println("height:" + height);
		System.out.println("width:" + width);
		System.out.println("pixelUnit:" + pixelUnit);
		System.out.println("puHeight:" + puHeight);
		System.out.println("puWidth:" + puWidth);
		System.out.println("camera:" + position.coordinates.toString());
		if (ClientScript.INSTANCE.playerEntity != null)
			center(((WorldEntityComponent) ClientScript.INSTANCE.playerEntity.getComponent(WorldEntityComponent.ID))
					.getPosition().get().coordinates);
	}

	public Integer getPixelUnit() {
		return pixelUnit;
	}

	public ScreenCoordinates getDrawCursorPosition() {
		return screenCoordinates;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemove(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender(Entity attachedEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		if (event instanceof Move) {
			onEventMove(attachedEntity, (Move) event);
		}
	}

	private void onEventMove(Entity attachedEntity, Move move) {
		if (attachedEntity.getUUID().equals(ClientScript.INSTANCE.uuid)) {
			Coordinates oldCoorodinates = move.getOldPosition().coordinates;
			CardinalOrientation oldOrientation = move.getOldPosition().orientation;
			Coordinates newCoorodinates = move.getNewPosition().coordinates;
			// déplacement de la caméra
			if (move.getCardinalOrientation() == oldOrientation) {
				Vector2D modifierMove = new Vector2D();
				// si on a changé de chunk
				if ((oldCoorodinates.getChunkX() != newCoorodinates.getChunkX()
						|| oldCoorodinates.getChunkY() != newCoorodinates.getChunkY())) {
					// clear des entités hors d champ de vue
					World.INSTANCE.getDynamicEntities().stream()
					.filter(dentity -> !((WorldEntityComponent) ClientScript.INSTANCE.playerEntity
							.getComponent(WorldEntityComponent.ID)).isInChunkRange(
									(WorldEntityComponent) dentity.getComponent(WorldEntityComponent.ID),
									Constraints.VIEW_RANGE))
					.forEach(notInRange -> World.INSTANCE.entityManager.removeEntity(notInRange));
					// recupérer les enités du chunk
					for (modifierMove.y = -1; modifierMove.y <= 1; ++modifierMove.y) {
						for (modifierMove.x = -1; modifierMove.x <= 1; ++modifierMove.x) {
							Coordinates coordinates = new Coordinates(newCoorodinates)
									.modChunkCoordinates(modifierMove, null);
							// TODO refaire propre
							ClientScript.INSTANCE.networkManager.connection
							.send(Messages.CHUNK_ENTITIES_GET.build(coordinates));
						}
					}
				}
				//récupération des chunks inconnus
				for (modifierMove.y = -1; modifierMove.y <= 1; ++modifierMove.y) {
					for (modifierMove.x = -1; modifierMove.x <= 1; ++modifierMove.x) {
						Coordinates coordinates = new Coordinates(newCoorodinates)
								.modChunkCoordinates(modifierMove, null);
						// TODO refaire propre
						if (World.INSTANCE.getNullChunk(coordinates) == null) {
							ClientScript.INSTANCE.networkManager.connection
							.send(Messages.CHUNK_TERRAIN_GET.build(coordinates));
						}
					}
				}
				move(move.getCardinalOrientation());
			}
		}
	}
}
