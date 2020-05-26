package sandbox.client.game.utils;

import java.lang.ref.WeakReference;

import sandbox.common.math.position.Position;
import sandbox.engine.math.CardinalOrientation;
import sandbox.engine.math.Smoother;
import sandbox.engine.misc.time.Timer;

public class MovementSmoother {
	public final WeakReference<Position> position;
	public Smoother xSmoother = new Smoother();
	public Smoother ySmoother = new Smoother();
	Timer actionTimer = new Timer();

	public MovementSmoother(WeakReference<Position> position) {
		this.position = position;
	}

	public void move(CardinalOrientation orientation, Integer pixelUnit, Long currentTimeMillis, Long moveDuration) {
		Position position = this.position.get();
		if (position == null || orientation != position.orientation)
			return;
		switch (position.orientation) {
		case NORTH:
			ySmoother.smooth(-pixelUnit, 0, currentTimeMillis, moveDuration);
			actionTimer.setDelay(currentTimeMillis, moveDuration);
			break;
		case SOUTH:
			ySmoother.smooth(+pixelUnit, 0, currentTimeMillis, moveDuration);
			actionTimer.setDelay(currentTimeMillis, moveDuration);
			break;
		case WEST:
			xSmoother.smooth(-pixelUnit, 0, currentTimeMillis, moveDuration);
			actionTimer.setDelay(currentTimeMillis, moveDuration);
			break;
		case EAST:
			xSmoother.smooth(+pixelUnit, 0, currentTimeMillis, moveDuration);
			actionTimer.setDelay(currentTimeMillis, moveDuration);
			break;
		default:
			break;
		}
	}
}
