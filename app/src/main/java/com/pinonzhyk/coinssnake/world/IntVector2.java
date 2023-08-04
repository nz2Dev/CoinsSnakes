package com.pinonzhyk.coinssnake.world;

public final class IntVector2 {
    public int x;
    public int y;

    public IntVector2() {
    }

    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntVector2(IntVector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(IntVector2 vector2) {
        this.x = vector2.x;
        this.y = vector2.y;
    }

    @Override
    public String toString() {
        return "IntVector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
