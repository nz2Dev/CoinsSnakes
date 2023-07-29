package com.pinonzhyk.coinssnake.world;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Scene {

    private List<WorldObject> worldObjects;

    public Scene(Collection<WorldObject> worldObjects) {
        this.worldObjects = new ArrayList<>(worldObjects);
    }

    public Collection<WorldObject> getAllObjects() {
        return worldObjects;
    }

    public boolean contains(WorldObject object) {
        return worldObjects.contains(object);
    }

    public void addObject(WorldObject object) {
        worldObjects.add(object);
    }
}
