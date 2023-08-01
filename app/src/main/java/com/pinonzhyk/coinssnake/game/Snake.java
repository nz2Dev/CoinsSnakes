package com.pinonzhyk.coinssnake.game;

import android.util.Log;

import com.pinonzhyk.coinssnake.world.IntVector2;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Snake extends WorldObject.Component implements WorldObject.LogicUpdateReceiver {

    private Deque<IntVector2> path;
    private List<WorldObject> tails;
    private Random random;
    private int direction;

    @Override
    protected void onInit() {
        random = new Random();
        path = new LinkedList<>();
        tails = new ArrayList<>();

        path.add(object().position);
        path.add(object().position.add(new IntVector2(-5, 0)));
        path.add(object().position.add(new IntVector2(-10, 0)));
        path.add(object().position.add(new IntVector2(-15, 0)));
        path.add(object().position.add(new IntVector2(-20, 0)));
        path.add(object().position.add(new IntVector2(-25, 0)));
        path.add(object().position.add(new IntVector2(-30, 0)));
        path.add(object().position.add(new IntVector2(-35, 0)));
        path.add(object().position.add(new IntVector2(-40, 0)));

        final WorldObject tail = new WorldObject(0, 0, 10, 10);
        world().instantiateWorldObject(tail);
        tails.add(tail);
        final WorldObject tail1 = new WorldObject(0, 0, 10, 10);
        world().instantiateWorldObject(tail1);
        tails.add(tail1);
        final WorldObject tail2 = new WorldObject(0, 0, 10, 10);
        world().instantiateWorldObject(tail2);
        tails.add(tail2);
    }

    public void changeDirection() {
        direction ++;
        direction = direction % 3;
    }

    @Override
    public void onUpdate(float timeSec) {
//        double randomSign = Math.copySign(1, Math.random() - 0.5);
//        double yStep = Math.round(Math.random() + 0.1d);
        double xStep = direction == 0 || direction == 1 ? 1 : 0;
        double yStep = direction == 1 || direction == 2 ? 1 : 0;
        IntVector2 newPoint = object().position.add(new IntVector2((int) (xStep * 2), (int) (yStep * 2)));
        object().position.set(newPoint);

        if (path.size() > tails.size() * 10) {
            path.removeLast();
            path.addFirst(newPoint);
        } else {
            path.addFirst(newPoint);
        }

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

    }
}
