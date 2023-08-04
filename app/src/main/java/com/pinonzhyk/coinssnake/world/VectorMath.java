package com.pinonzhyk.coinssnake.world;

public class VectorMath {

    public static Vector2 add(Vector2 vector, float x, float y, Vector2 resultRef) {
        resultRef.setTo(vector.x + x, vector.y + y);
        return resultRef;
    }
}
