package com.pinonzhyk.coinssnake;

import java.util.Collection;
import java.util.List;

public class Scene {

    private List<WorldObject> worldObjects;

    public Scene(List<WorldObject> worldObjects) {
        this.worldObjects = worldObjects;
    }

    public Collection<WorldObject> getAllObjects() {
        return worldObjects;
    }

}
