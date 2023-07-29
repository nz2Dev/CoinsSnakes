package com.pinonzhyk.coinssnake;

import java.util.Collection;

public class World {
    private int boundsWidthUnits;
    private int boundsHeightUnits;
    private Scene scene;

    public World(int boundsWidthUnits, int boundsHeightUnits, Scene scene) {
        this.boundsWidthUnits = boundsWidthUnits;
        this.boundsHeightUnits = boundsHeightUnits;
        this.scene = scene;
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

    public void update(float timeSec, float deltaTime) {
        for (WorldObject worldObject : scene.getAllObjects()) {
            worldObject.update(timeSec);
        }
    }
}
