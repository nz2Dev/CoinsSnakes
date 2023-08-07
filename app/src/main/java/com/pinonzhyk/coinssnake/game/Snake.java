package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.GraphicComponent;
import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.VectorMath;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Snake extends WorldObject.Component implements WorldObject.UpdateReceiver, WorldObject.FixedTimeUpdateReceiver {

    private Deque<Vector2> path;
    private List<WorldObject> tails;
    private Vector2 direction;
    private float speed;
    private float tailSize;
    private float offset;
    private int pathSegmentOffset;

    @Override
    protected void onInit() {
        path = new ArrayDeque<>();
        tails = new ArrayList<>();
        direction = new Vector2(1, 0);
        path.add(object().position);
        pathSegmentOffset = 1;

        tailSize = world().getBoundsWidthUnits() * 0.02f;
        speed = world().getBoundsWidthUnits() * 0.1f;
        offset = tailSize * 2f;

        setTailsCapacity(5);
    }

    private void setTailsCapacity(int capacity) {
        if (tails.size() < capacity) {
            final int newTailsCount = capacity - tails.size();
            final float surfaceSize = tailSize * 4;
            for (int i = 0; i < newTailsCount; i++) {
                final WorldObject tail = new WorldObject(0, 0, new Vector2(surfaceSize, surfaceSize), null);
                tail.addComponent(new GraphicComponent(tailSize));
                tail.addComponent(new SnakeTail(this::onSnakeClicked));
                world().instantiateWorldObject(tail);
                tails.add(tail);
            }
        }
    }

    public void changeDirection() {
        rotate(true);
    }

    private void rotate(boolean right) {
        if (direction.x > 0) {
            direction.setTo(0, right ? 1 : -1);
        } else if (direction.x < 0) {
            direction.setTo(0, right ? -1 : 1);
        } else if (direction.y > 0) {
            direction.setTo(right ? -1 : 1, 0);
        } else if (direction.y < 0) {
            direction.setTo(right ? 1 : -1, 0);
        } else {
            direction.setTo(1, 0);
        }
    }

    private void onSnakeClicked() {
        changeDirection();
    }

    @Override
    public void onFixedUpdate(float fixedTimeSec, float deltaTimeStep) {
        Vector2 castPoint = VectorMath.add(
                object().position,
                direction.x * 0.1f,
                direction.y * 0.1f,
                new Vector2());

        WorldObject obstacle = world().pointCastObject(castPoint);
        if (obstacle != null) {
            rotate(Math.random() > 0.5f);
            castPoint = VectorMath.add(
                    obstacle.position,
                    direction.x * 0.1f,
                    direction.y * 0.1f,
                    castPoint);
            obstacle = world().pointCastObject(castPoint);
            if (obstacle != null) {
                return;
            }
        }

        moveAtDirection(deltaTimeStep);
    }

    private void moveAtDirection(float deltaTimeStep) {
        float moveDelta = deltaTimeStep * speed;
        final Vector2 newPoint = VectorMath.add(
                object().position,
                /*x*/ direction.x * moveDelta,
                /*y*/ direction.y * moveDelta,
                new Vector2());

        object().position.setFrom(newPoint);
        appendPath(moveDelta, newPoint);
    }

    private void appendPath(float moveDelta, Vector2 newPoint) {
        // if the delta value is closer to the offset or greater
        // then the path precision is closer to zero
        // and so the offset then become the max(delta, offset)
        // to be so, the speed must be more or around the world.bounds * fps
        // which is not the case for the most part, and so this case should not be an issue
        // in this context, but could potentially in other circumstances

        final float segmentsPerOffset = offset / moveDelta;
        pathSegmentOffset = Math.round(segmentsPerOffset);
        pathSegmentOffset = Math.max(pathSegmentOffset, 1);

        if (path.size() > tails.size() * pathSegmentOffset) {
            // we prevent infinite grow of path deque, but we don't shrink it otherwise,
            // which should be ok for the most part as well,
            path.removeLast();
            // todo and we can reuse the last vector that we will throw away, to avoid allocation
            // but it introduce some glitches that need to be investigated further
        }

        path.addFirst(newPoint);
    }

    @Override
    public void onUpdate(float timeSec) {
        int tailIndex = 0;
        int pointIndex = 0;
        for (Vector2 point : path) {
            // on each tail point after skipping the offset which should be constant size,
            // set next indexed tail position to that vector value
            if (pointIndex % pathSegmentOffset == 0 && tailIndex < tails.size()) {
                final WorldObject tailPart = tails.get(tailIndex);
                tailPart.position.setFrom(point);
                tailIndex++;
            }
            pointIndex++;
        }

        // if tails is not counted to the end because there is less path points than tails
        // we use last known path points which should point at the end of the snake's path
        // e.s should be the oldest. This is a workaround before or if we will implement
        // manual path generation to adjust the path to the amount of tails
        if (tailIndex < tails.size() - 1 && !path.isEmpty()) {
            final Vector2 lastPoint = path.getLast();
            for (; tailIndex < tails.size(); tailIndex++) {
                tails.get(tailIndex).position.setFrom(lastPoint);
            }
        }
    }

    private static class SnakeTail extends WorldObject.Component implements WorldObject.ClickEventReceiver {
        private final SnakeTailClickListener clickListener;

        public SnakeTail(SnakeTailClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClickEvent(float x, float y) {
            clickListener.onTailClicked();
        }
    }

    private interface SnakeTailClickListener {
        void onTailClicked();
    }
}
