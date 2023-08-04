package com.pinonzhyk.coinssnake.world;

public class VectorMath {

    public static IntVector2 add(IntVector2 vector, int x, int y, IntVector2 resultRef) {
        resultRef.set(vector.x + x, vector.y + y);
        return resultRef;
    }
}
