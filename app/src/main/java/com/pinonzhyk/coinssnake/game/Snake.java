package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.IntVector2;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Snake extends WorldObject.Component implements WorldObject.UpdateReceiver, WorldObject.FixedTimeUpdateReceiver {

    private Deque<IntVector2> path;
    private List<WorldObject> tails;
    private int direction;
    private float speed = 10;

    @Override
    protected void onInit() {
        path = new LinkedList<>();
        tails = new ArrayList<>();
        path.add(object().position);
        setTailsCapacity(2);
    }

    private void setTailsCapacity(int capacity) {
        if (tails.size() < capacity) {
            final int newTailsCount = capacity - tails.size();
            for (int i = 0; i < newTailsCount; i++) {
                final WorldObject tail = new WorldObject(0, 0, 10, 10);
                world().instantiateWorldObject(tail);
                tails.add(tail);
            }
        }
    }

    public void changeDirection() {
        direction ++;
        direction = direction % 3;
        speed += 5;
        setTailsCapacity((int) (speed / 15));
    }

    @Override
    public void onFixedUpdate(float fixedTimeSec, float deltaTimeStep) {
        double xStep = direction == 0 || direction == 1 ? 1 : 0;
        double yStep = direction == 1 || direction == 2 ? 1 : 0;
        float delta = (float) Math.ceil(deltaTimeStep * speed);

        // todo refactor vector math to be static methods with value type params
        final IntVector2 newPoint = object().position.add(
                new IntVector2((int) (xStep * delta), (int) (yStep * delta)));
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
        for (IntVector2 point : path) {
            if (pointIndex % 5 == 0 && tailIndex < tails.size()) {
                final WorldObject tailPart = tails.get(tailIndex);
                tailPart.position.set(point);
                tailIndex++;
            }
            pointIndex++;
        }

        // if tails is not counted to the end
        if (tailIndex < tails.size() - 1 && !path.isEmpty()) {
            final IntVector2 lastPoint = path.getLast();
            for (; tailIndex < tails.size(); tailIndex++) {
                tails.get(tailIndex).position.set(lastPoint);
            }
        }
    }

}
