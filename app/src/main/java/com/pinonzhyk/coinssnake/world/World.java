package com.pinonzhyk.coinssnake.world;

import java.util.Collection;

public class World {
    private int boundsWidthUnits;
    private int boundsHeightUnits;
    private ClickEventsListener clickEventsListener;
    private Scene scene;

    public World(int boundsWidthUnits, int boundsHeightUnits, Scene scene) {
        this.boundsWidthUnits = boundsWidthUnits;
        this.boundsHeightUnits = boundsHeightUnits;
        this.scene = scene;
    }

    public void init() {
        for (WorldObject worldObject : scene.getAllObjects()) {
            worldObject.init();
        }
    }

    public int getBoundsWidthUnits() {
        return boundsWidthUnits;
    }

    public int getBoundsHeightUnits() {
        return boundsHeightUnits;
    }

    public Collection<WorldObject> getObjects() {
        return scene.getAllObjects();
    }

    public void setClickEventsListener(ClickEventsListener clickEventsListener) {
        this.clickEventsListener = clickEventsListener;
    }

    public void handleClickInput(int x, int y) {
        WorldObject eventReceiver = null;
        for (WorldObject worldObject : scene.getAllObjects()) {
            if (!worldObject.inputSurfaceSize.isZero()) {
                int halfSurfaceX = worldObject.inputSurfaceSize.x / 2;
                int halfSurfaceY = worldObject.inputSurfaceSize.y / 2;
                if (x > worldObject.position.x - halfSurfaceX
                        && x < worldObject.position.x + halfSurfaceX
                        && y > worldObject.position.y - halfSurfaceY
                        && y < worldObject.position.y + halfSurfaceY) {
                    worldObject.receiveClickEvent(x, y);
                    eventReceiver = worldObject;
                    break;
                }
            }
        }

        if (clickEventsListener != null) {
            clickEventsListener.onClickEvent(eventReceiver, x, y);
        }
    }

    public void update(float timeSec, float deltaTime) {
        for (WorldObject worldObject : scene.getAllObjects()) {
            worldObject.update(timeSec);
        }
    }

    public interface ClickEventsListener {
        void onClickEvent(WorldObject receiver, int x, int y);
    }

}
