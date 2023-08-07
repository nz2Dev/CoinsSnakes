package com.pinonzhyk.coinssnake.world;

public final class RectUtils {

    static boolean isPointInsideSurface(float x, float y, Vector2 position, Vector2 surfaceSize) {
        return isPointInsideSurface(x, y, position.x, position.y, surfaceSize.x, surfaceSize.y);
    }

    static boolean isPointInsideSurface(float x, float y, float centerX, float centerY, float width, float height) {
        final float halfWidth = width / 2f;
        final float halfHeight = height / 2f;
        return IsPointInsideRect(
                x,
                y,
                centerX - halfWidth,
                centerY - halfHeight,
                centerX + halfWidth,
                centerY + halfHeight
        );
    }

    static boolean IsPointInsideRect(float x, float y, float left, float top, float right, float bottom) {
        return x > left && x < right && y > top && y < bottom;
    }

}
