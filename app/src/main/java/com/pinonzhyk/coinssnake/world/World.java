package com.pinonzhyk.coinssnake.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    private final int boundsWidthUnits;
    private final int boundsHeightUnits;
    private final Scene scene;
    private final List<System> systems;

    public World(int boundsWidthUnits, int boundsHeightUnits) {
        this.boundsWidthUnits = boundsWidthUnits;
        this.boundsHeightUnits = boundsHeightUnits;
        this.scene = new Scene();
        this.systems = new ArrayList<>();
    }

    public int getBoundsWidthUnits() {
        return boundsWidthUnits;
    }

    public int getBoundsHeightUnits() {
        return boundsHeightUnits;
    }

    public void addSystem(System system) {
        if (systems.contains(system)) {
            throw new RuntimeException("already contains: " + system);
        }
        systems.add(system);
        system.attach(this);
    }

    public <T extends System> T findSystem(Class<T> type) {
        for (System system : systems) {
            if (type.isAssignableFrom(system.getClass())) {
                return (T) system;
            }
        }
        return null;
    }

    public void instantiateWorldObject(WorldObject object) {
        if (!scene.contains(object)) {
            scene.addObject(object);
            object.init(this);
        }
    }

    public Collection<WorldObject> getObjects() {
        return scene.getAllObjects();
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

        for (System system : systems) {
            if (system instanceof InputSystem) {
                InputSystem inputSystem = (InputSystem) system;
                inputSystem.onClick(eventReceiver, x, y);
            }
        }
    }

    public void update(float timeSec, float deltaTime) {
        for (WorldObject worldObject : scene.getAllObjects()) {
            worldObject.update(timeSec);
        }

        for (System system : systems) {
            if (system instanceof UpdateSystem) {
                ((UpdateSystem) system).onUpdate(timeSec, deltaTime);
            }
        }
    }

    public static abstract class System {
        private World world;
        private void attach(World world) {
            this.world = world;
        }
        protected World world() {
            return world;
        }
    }

    public interface UpdateSystem {
        void onUpdate(float timeSec, float deltaTime);
    }

    public interface InputSystem {
        void onClick(WorldObject receiver, int x, int y);
    }
}
