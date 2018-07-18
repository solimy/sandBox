package sandbox.client.game.components;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import sandbox.client.ClientScript;
import sandbox.client.game.render.CameraScript;
import sandbox.client.game.utils.MovementSmoother;
import sandbox.client.resources.animators.AnimationsIds;
import sandbox.common.game.components.WorldEntityComponent;
import sandbox.common.game.events.Move;
import sandbox.common.math.position.Coordinates;
import sandbox.common.math.position.Position;
import sandbox.common.protocol.Messages;
import sandbox.common.protocol.messages.entity.EntityDeathMessage;
import sandbox.common.world.Constraints;
import sandbox.common.world.elements.entity.BodyPart;
import sandbox.common.world.elements.entity.state.EntityState;
import sandbox.common.world.model.World;
import sandbox.engine.Engine;
import sandbox.engine.game.Component;
import sandbox.engine.game.Entity;
import sandbox.engine.game.Event;
import sandbox.engine.graphic.GraphicApplication;
import sandbox.engine.graphic.drawable.animation.Animator2D;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Vector2D;

public class GraphicEntityComponent implements Component {
	public static final String ID = "gec";
	private Map<BodyPart, Animator2D> animators = new HashMap<>();
	private final MovementSmoother movementSmoother;
	private final WeakReference<Position> position;

	public GraphicEntityComponent(WeakReference<Position> position) {
		movementSmoother = new MovementSmoother(position);
		this.position = position;
	}

	public GraphicEntityComponent setAnimators(Map<BodyPart, Animator2D> animators) {
		this.animators = animators;
		return this;
	}

	@Override
	public String getId() {
		return ID;
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
		final Long currentTimeMillis = Engine.Clock.INSTANCE.getCurrentTimeMillis();
		final Integer pixelUnit = CameraScript.INSTANCE.getPixelUnit();
		final CameraScript.ScreenCoordinates screenCoordinates = CameraScript.INSTANCE.getDrawCursorPosition();
		animators.forEach((key, animator) -> {
			animator.getFrame(position.get().orientation).setFit(pixelUnit, pixelUnit)
					.setXY(screenCoordinates.x - movementSmoother.xSmoother.get(currentTimeMillis),
							screenCoordinates.y - movementSmoother.ySmoother.get(currentTimeMillis))
					.render(GraphicApplication.getGraphicsContext());
		});
	}

	@Override
	public void onUse(Entity attachedEntity, Entity user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(Entity attachedEntity, Event event) {
		if (event instanceof Move) {
			onEventMove(attachedEntity, (Move) event);
		} else if (event instanceof EntityDeathMessage) {
			onEventDeath(attachedEntity, (EntityDeathMessage) event);
		}
	}

	private void onEventDeath(Entity attachedEntity, EntityDeathMessage deathMessage) {		
		animators.forEach((bodyPart, animation) -> animation.play(AnimationsIds.DEATH, false));
	}
	
	private void onEventMove(Entity attachedEntity, Move move) {
		Coordinates oldCoorodinates = move.getOldPosition().coordinates;
		CardinalOrientation oldOrientation = move.getOldPosition().orientation;
		movementSmoother.move(oldOrientation, CameraScript.INSTANCE.getPixelUnit(),
				Engine.Clock.INSTANCE.getCurrentTimeMillis(), EntityState.WALK.getMaximumDuration());
		animators.forEach((bodyPart, animator) -> animator.play(AnimationsIds.WALK, false));
		if ((oldCoorodinates.getChunkX() != position.get().coordinates.getChunkX()
				|| oldCoorodinates.getChunkY() != position.get().coordinates.getChunkY())
				&& !((WorldEntityComponent) ClientScript.INSTANCE.playerEntity
						.getComponent(WorldEntityComponent.ID)).getPosition().get().coordinates
								.isInChunkRange(position.get().coordinates, Constraints.VIEW_RANGE)) {
			World.INSTANCE.entityManager.removeEntity(attachedEntity);
			return;
		}
		CameraScript.INSTANCE.onEvent(attachedEntity, move);
	}
}
