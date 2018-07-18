//package sandbox.client.ui.display;
//
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//
//import javax.swing.JPanel;
//
//import sandbox.client.Client;
//import sandbox.client.ui.display.drawable.utils.MovementSmoother;
//import sandbox.client.world.elements.VisualEntity;
//import sandbox.common.math.Smoother;
//import sandbox.common.math.Vector2D;
//import sandbox.common.math.position.Coordinates;
//import sandbox.common.math.position.Orientation;
//import sandbox.common.math.position.Position;
//import sandbox.common.misc.Timer;
//import sandbox.common.world.elements.entity.state.EntityState;
//import sandbox.common.world.model.Cell;
//import sandbox.common.world.model.Chunk;
//import sandbox.common.world.model.World;
//
//public class Camera extends JPanel {
//
//	final Client client;
//	World world = null;
//	Integer height = 0;
//	Integer width = 0;
//	Position position = new Position(new Coordinates(Chunk.WIDTH, Chunk.LENGTH), Orientation.ANY);
//	Integer pixelUnitRatio = 11; // number of visible cells
//	Integer pixelUnit = 1; // size of a cell
//	Integer puHeight = 0;
//	Integer puWidth = 0;
//	final MovementSmoother movementSmoother;
//	boolean isRunning = false;
//
//	private class ScreenCoordinates {
//		public Integer x = null;
//		public Integer y = null;
//
//		public ScreenCoordinates(Coordinates coordinates) {
//			x = (coordinates.getWorldX() - Camera.this.position.coordinates.getWorldX()) * pixelUnit;
//			y = height - pixelUnit - ((coordinates.getWorldY() - Camera.this.position.coordinates.getWorldY()) * pixelUnit);
//		}
//	}
//
//	public void move(Coordinates coordinates) {
//		this.position.coordinates.copy(coordinates);
//	}
//
//	public void center(Coordinates coordinates) {
//		this.position.coordinates.copy(coordinates).modWorldCoordinates(new Vector2D(-puWidth / 2, -puHeight / 2));
//	}
//
//	public void move(Orientation move) {
////		if (!actionTimer.isFinished(client.world.getUpdateTimeMillis()))
////			return;
//		Long currentTimeMillis = client.world.getUpdateTimeMillis();
//		switch (move) {
//		case NORTH:
//			position.coordinates.setWorldY(position.coordinates.getWorldY() + 1);
//			position.orientation = Orientation.NORTH;
//			break;
//		case SOUTH:
//			position.coordinates.setWorldY(position.coordinates.getWorldY() - 1);
//			position.orientation = Orientation.SOUTH;
//			break;
//		case WEST:
//			position.coordinates.setWorldX(position.coordinates.getWorldX() - 1);
//			position.orientation = Orientation.WEST;
//			break;
//		case EAST:
//			position.coordinates.setWorldX(position.coordinates.getWorldX() + 1);
//			position.orientation = Orientation.EAST;
//			break;
//		default:
//			break;
//		}
//		movementSmoother.move(move, pixelUnit, currentTimeMillis, EntityState.MOVE.getMaximumDuration());
//	}
//
//	public Camera(Client client) {
//		this.client = client;
//		setBackground(Color.BLACK);
//		movementSmoother = new MovementSmoother(position);
//	}
//
//	public void watch(World world) {
//		this.world = world;
//	}
//
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		if (world == null)
//			return;
//		client.world.timeUpdate();
//		Long currentTimeMillis = client.world.getUpdateTimeMillis();
//		Graphics2D g2 = (Graphics2D) g;
//		Integer camXS = movementSmoother.xSmoother.get(currentTimeMillis);
//		Integer camYS = movementSmoother.ySmoother.get(currentTimeMillis);
//		Vector2D mod = new Vector2D();
//		// draw décor
//		for (mod.y = -2; mod.y < puHeight + 2; ++mod.y) {
//			for (mod.x = -2; mod.x < puWidth + 2; ++mod.x) {
//				Coordinates renderCoordinates = new Coordinates(position.coordinates).modWorldCoordinates(mod);
//				Cell cell = world.getNullCell(renderCoordinates);
//				if (cell == null) {
//					continue;
//				}
//				ScreenCoordinates screenCoordinates = new ScreenCoordinates(renderCoordinates);
//				screenCoordinates.x += camXS;
//				screenCoordinates.y += camYS;
//				// draw terrain
//				g2.drawImage(TerrainRenderer.getSprite(cell.getTerrainType()).scaled(pixelUnit, pixelUnit),
//						screenCoordinates.x, screenCoordinates.y, null);
//			}
//		}
//		// draw entities
//		for (mod.y = -2; mod.y < puHeight + 2; ++mod.y) {
//			for (mod.x = -2; mod.x < puWidth + 2; ++mod.x) {
//				Coordinates renderCoordinates = new Coordinates(position.coordinates).modWorldCoordinates(mod);
//				Cell cell = world.getNullCell(renderCoordinates);
//				if (cell == null) {
//					continue;
//				}
//				ScreenCoordinates screenCoordinates = new ScreenCoordinates(renderCoordinates);
//				screenCoordinates.x += camXS;
//				screenCoordinates.y += camYS;
//				cell.getDynamicEntities().values().forEach(entity -> {
//					final VisualEntity visualEntity = (VisualEntity) entity;
//					final Long ellapsedAnimationTime = currentTimeMillis
//							- visualEntity.stateManager.getLastStateTimeMillis();
//					visualEntity.animators.forEach((key, animator) -> {
//						g2.drawImage(
//								animator.getFrame(visualEntity.stateManager.getState(currentTimeMillis),
//										visualEntity.position.orientation, ellapsedAnimationTime)
//										.scaled(pixelUnit, pixelUnit),
//								screenCoordinates.x - visualEntity.movementSmoother.xSmoother.get(currentTimeMillis),
//								screenCoordinates.y - visualEntity.movementSmoother.ySmoother.get(currentTimeMillis), null);
//					});
//				});
//			}
//		}
//	}
//
//	public void updateSize() {
//		height = getSize().height;
//		width = getSize().width;
//		pixelUnit = height > width ? width / pixelUnitRatio : height / pixelUnitRatio;
//		pixelUnit = pixelUnit == 0 ? 1 : pixelUnit;
//		puHeight = height / pixelUnit;
//		puWidth = width / pixelUnit;
//		System.out.println("height:" + height);
//		System.out.println("width:" + width);
//		System.out.println("pixelUnit:" + pixelUnit);
//		System.out.println("puHeight:" + puHeight);
//		System.out.println("puWidth:" + puWidth);
//		System.out.println("camera:" + position.coordinates.toString());
//		if (client.gameEntity != null)
//			center(client.gameEntity.position.coordinates);
//	}
//
//	public Integer getPixelUnit() {
//		return pixelUnit;
//	}
//}
