package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.GraphicComponent;
import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.VectorMath;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Snake extends WorldObject.Component implements WorldObject.UpdateReceiver, WorldObject.FixedTimeUpdateReceiver {

    private SegmentedPath path;
    private List<WorldObject> tails;
    private Vector2 direction;
    private float speed;
    private float tailSize;
    private float lastDirectionChangeTime;
    private float directionChangeIntervalSec;

    @Override
    protected void onInit() {
        path = new SegmentedPath(object().position);
        tails = new ArrayList<>();
        direction = new Vector2(1, 0);

        tailSize = world().getBoundsWidthUnits() * 0.02f;
        speed = world().getBoundsWidthUnits() * 0.1f;
        path.setSegmentDistance(tailSize * 2f);

        directionChangeIntervalSec = 3f;
        setTailsCapacity(5);
    }

    private void setTailsCapacity(int capacity) {
        path.setSegmentsCount(capacity);
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

    private void destroyLastTail() {
        if (tails.size() > 2) {
            WorldObject lastTail = tails.get(tails.size() - 1);
            tails.remove(lastTail);
            world().destroy(lastTail);
            path.setSegmentsCount(tails.size());
        }
    }

    private void changeDirectionRandomly() {
        rotate(Math.random() > 0.5d);
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
        destroyLastTail();
    }

    @Override
    public void onFixedUpdate(float fixedTimeSec, float deltaTimeStep) {
        Vector2 castPoint = VectorMath.add(
                object().position,
                direction.x * 0.1f,
                direction.y * 0.1f,
                new Vector2());

        if (lastDirectionChangeTime + directionChangeIntervalSec < fixedTimeSec) {
            lastDirectionChangeTime = fixedTimeSec;
            changeDirectionRandomly();
        }

        WorldObject obstacle = world().pointCastObject(castPoint);
        if (obstacle != null) {
            changeDirectionRandomly();
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
        path.append(direction.x * moveDelta, direction.y * moveDelta);
        object().position.setFrom(path.tip());
    }

    @Override
    public void onUpdate(float timeSec) {
        final Vector2[] segments = path.segmented();
        for (int segmentIndex = 0; segmentIndex < segments.length; segmentIndex++) {
            tails.get(segmentIndex).position.setFrom(segments[segmentIndex]);
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
