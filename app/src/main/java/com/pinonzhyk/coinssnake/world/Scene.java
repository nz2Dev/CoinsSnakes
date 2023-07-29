package com.pinonzhyk.coinssnake.world;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
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
