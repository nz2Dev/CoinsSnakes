package com.pinonzhyk.coinssnake;

import java.util.Collection;
import java.util.List;

public class Scene {

    private int widthUnits;
    private int heightUnits;
    private List<SceneObject> sceneObjects;

    public Scene(int widthUnits, int heightUnits, List<SceneObject> sceneObjects) {
        this.widthUnits = widthUnits;
        this.heightUnits = heightUnits;
        this.sceneObjects = sceneObjects;
    }

    public int getWidthUnits() {
        return widthUnits;
    }

    public int getHeightUnits() {
        return heightUnits;
    }

    public Collection<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

}
