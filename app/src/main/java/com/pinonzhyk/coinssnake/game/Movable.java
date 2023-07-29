package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.IntVector2;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class Movable extends WorldObject.LogicComponent {
    private float startTime = -1;
    private int startX;

    @Override
    protected void onUpdate(float timeSec) {
        final IntVector2 position = object().position;

        if (startTime == -1) {
            startTime = timeSec;
            startX = object().position.x;
            return;
        }

        float diff = timeSec - startTime;
        position.x = (int) (startX + diff * 200);
        if (position.x > 1000) {
            position.x = startX;
            startTime = timeSec;
        }
    }
}
