package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class Flower extends WorldObject.Component implements WorldObject.UpdateReceiver {

    private float lastYieldCheckTime;
    private float yieldInterval;
    private int earnPerYield;

    public Flower() {
        lastYieldCheckTime = -1;
        yieldInterval = 3f;
        earnPerYield = 1;
    }

    @Override
    public void onUpdate(float timeSec) {
        if (lastYieldCheckTime < 0) {
            lastYieldCheckTime = timeSec;
        }

        if (lastYieldCheckTime + yieldInterval < timeSec) {
            lastYieldCheckTime = timeSec;
            world().findSystem(EconomySystem.class).earn(earnPerYield);
        }
    }
}
