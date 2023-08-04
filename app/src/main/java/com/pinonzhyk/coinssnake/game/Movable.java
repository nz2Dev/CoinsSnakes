package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.WorldObject.*;

public class Movable extends Component implements UpdateReceiver, ClickEventReceiver {
    private float startTime = -1;
    private float startX;
    private float speed;

    @Override
    protected void onInit() {
        speed = world().getBoundsWidthUnits() * 0.05f;
    }

    @Override
    public void onUpdate(float timeSec) {
        final Vector2 position = object().position;

        if (startTime == -1) {
            startTime = timeSec;
            startX = object().position.x;
            return;
        }

        float diff = timeSec - startTime;
        position.x = (int) (startX + diff * speed);
        if (position.x > world().getBoundsWidthUnits()) {
            position.x = startX;
            startTime = timeSec;
        }
    }

    @Override
    public void onClickEvent(float x, float y) {
        speed *= 1.1f;
    }
}
