package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.VectorMath;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Snake extends WorldObject.Component implements WorldObject.UpdateReceiver, WorldObject.FixedTimeUpdateReceiver {

    private Deque<Vector2> path;
    private List<WorldObject> tails;
    private int direction;
    private float speed = 1;

    @Override
    protected void onInit() {
        path = new LinkedList<>();
        tails = new ArrayList<>();
        path.add(object().position);
        setTailsCapacity(2);
    }

    private void setTailsCapacity(int capacity) {
        if (tails.size() < capacity) {
            final float tailSize = world().getBoundsWidthUnits() * 0.01f;
            final int newTailsCount = capacity - tails.size();
            for (int i = 0; i < newTailsCount; i++) {
                final WorldObject tail = new WorldObject(0, 0, tailSize, tailSize);
                world().instantiateWorldObject(tail);
                tails.add(tail);
            }
        }
    }

    public void changeDirection() {
        direction ++;
        direction = direction % 3;
        speed *= 1.2f;
        setTailsCapacity((int) speed);
    }

    @Override
    public void onFixedUpdate(float fixedTimeSec, float deltaTimeStep) {
        float xStep = direction == 0 || direction == 1 ? 1 : 0;
        float yStep = direction == 1 || direction == 2 ? 1 : 0;
        float delta = deltaTimeStep * speed;

        final Vector2 newPoint = VectorMath.add(
                object().position,
                /*x*/ xStep * delta,
                /*y*/ yStep * delta,
                new Vector2());

        object().position.set(newPoint);

        if (path.size() > tails.size() * 10) {
            path.removeLast();
        }
        path.addFirst(newPoint);
    }

    @Override
    public void onUpdate(float timeSec) {
        int tailIndex = 0;
        int pointIndex = 0;
        for (Vector2 point : path) {
            // on each fifth path's point set next indexed tail position to that vector value
            // wheres 5 is the offset between tails
            if (pointIndex % 5 == 0 && tailIndex < tails.size()) {
                final WorldObject tailPart = tails.get(tailIndex);
                tailPart.position.set(point);
                tailIndex++;
            }
            pointIndex++;
        }

        // if tails is not counted to the end because there is less path points than tails
        // we use last known path points which should point at the end of the snake's path
        // e.s should be the oldest
        if (tailIndex < tails.size() - 1 && !path.isEmpty()) {
            final Vector2 lastPoint = path.getLast();
            for (; tailIndex < tails.size(); tailIndex++) {
                tails.get(tailIndex).position.set(lastPoint);
            }
        }
    }

}
