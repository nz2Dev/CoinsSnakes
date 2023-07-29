package com.pinonzhyk.coinssnake;

public class WorldObject {

    private int x;
    private int y;

    public WorldObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private float startTime = -1;
    private int startX;

    public void update(float time) {
        if (startTime == -1) {
            startTime = time;
            startX = x;
            return;
        }

        float diff = time - startTime;
        x = (int) (startX + diff * 200);
        if (x > 1000) {
            x = startX;
            startTime = time;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
