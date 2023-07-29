package com.pinonzhyk.coinssnake.world;

public final class IntVector2 {
    public int x;
    public int y;

    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntVector2(IntVector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
}