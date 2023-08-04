package com.pinonzhyk.coinssnake.world;

public final class Vector2 {
    public float x;
    public float y;

    public Vector2() {
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 vector2) {
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
