package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.IntVector2;
import com.pinonzhyk.coinssnake.world.WorldObject.*;

public class Movable extends Component implements UpdateReceiver, ClickEventReceiver {
    private float startTime = -1;
    private int startX;
    private int speed = 200;

    @Override
    public void onUpdate(float timeSec) {
        final IntVector2 position = object().position;

        if (startTime == -1) {
            startTime = timeSec;
            startX = object().position.x;
            return;
        }

        float diff = timeSec - startTime;
        position.x = (int) (startX + diff * speed);
        if (position.x > 1000) {
            position.x = startX;
            startTime = timeSec;
        }
    }

    @Override
    public void onClickEvent(int x, int y) {
        speed += 50;
    }
}
