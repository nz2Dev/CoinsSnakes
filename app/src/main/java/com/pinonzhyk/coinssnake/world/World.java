package com.pinonzhyk.coinssnake.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    private final int boundsWidthUnits;
    private final int boundsHeightUnits;
    private final Scene scene;
    private final List<System> systems;
    private float lastUpdateTime = -1f;
    private float lastFixedUpdateTime;
    private final float fixedUpdateStepTime = 1f / 50f;
    private final List<WorldObject> colliders;
    private final List<WorldObject> postponedInstantiationObjects;
    private final List<WorldObject> postponedDestructionObjects;
    private boolean changesPostponed;

    public World(int boundsWidthUnits, int boundsHeightUnits) {
        this.boundsWidthUnits = boundsWidthUnits;
        this.boundsHeightUnits = boundsHeightUnits;
        this.scene = new Scene();
        this.systems = new ArrayList<>();
        this.colliders = new ArrayList<>();
        this.postponedInstantiationObjects = new ArrayList<>();
        this.postponedDestructionObjects = new ArrayList<>();
    }

    public int getBoundsWidthUnits() {
        return boundsWidthUnits;
    }

    public int getBoundsHeightUnits() {
        return boundsHeightUnits;
    }

    public WorldObject pointCastObject(Vector2 point) {
        return pointCastObject(point.x, point.y);
    }

    public WorldObject pointCastObject(float x, float y) {
        for (WorldObject object : colliders) {
            if (!object.canCollide()) {
                throw new RuntimeException("non collidable object is in colliders list");
            }

            if (RectUtils.isPointInsideSurface(x, y,
                    object.position.x, object.position.y,
                    object.colliderSurfaceSize.x, object.colliderSurfaceSize.y)) {
                return object;
            }
        }
        return null;
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
        if (changesPostponed) {
            postponedInstantiationObjects.add(object);
            return;
        }
        if (!scene.contains(object)) {
            scene.addObject(object);
            object.init(this);
        }
    }

    private void postponeStructureChanges() {
        changesPostponed = true;
    }

    private void runPostponedStructureChanges() {
        changesPostponed = false;
        if (postponedInstantiationObjects.size() > 0) {
            for (WorldObject object : postponedInstantiationObjects) {
                if (!scene.contains(object)) {
                    scene.addObject(object);
                    object.init(this);
                }
            }
            postponedInstantiationObjects.clear();
        }
        if (postponedDestructionObjects.size() > 0) {
            for (WorldObject object : postponedDestructionObjects) {
                if (!scene.contains(object)) {
                    throw new RuntimeException("object doesn't not instantiated in this world");
                }
                scene.removeObject(object);
                object.destroy();
            }
            postponedDestructionObjects.clear();
        }
    }

    public void destroy(WorldObject worldObject) {
        if (changesPostponed) {
            postponedDestructionObjects.add(worldObject);
            return;
        }
        if (!scene.contains(worldObject)) {
            throw new RuntimeException("object doesn't not instantiated in this world");
        }
        scene.removeObject(worldObject);
        worldObject.destroy();
    }

    public Collection<WorldObject> getObjects() {
        return scene.getAllObjects();
    }

    public void handleClickInput(float xUnits, float yUnits) {
        WorldObject eventReceiver = null;
        for (WorldObject worldObject : scene.getAllObjects()) {
            if (worldObject.canBeClicked()) {
                if (RectUtils.isPointInsideSurface(xUnits, yUnits,
                        worldObject.position, worldObject.inputSurfaceSize)) {
                    worldObject.receiveClickEvent(xUnits, yUnits);
                    eventReceiver = worldObject;
                    break;
                }
            }
        }

        for (System system : systems) {
            if (system instanceof InputSystem) {
                InputSystem inputSystem = (InputSystem) system;
                inputSystem.onClick(eventReceiver, xUnits, yUnits);
            }
        }
    }

    public void update(float timeSec) {
        if (lastUpdateTime == -1) {
            lastUpdateTime = timeSec;
            lastFixedUpdateTime = timeSec;
        }

        float fixedDeltaTime = timeSec - lastFixedUpdateTime;
        while (fixedDeltaTime > fixedUpdateStepTime) {
            lastFixedUpdateTime += fixedUpdateStepTime;
            buildColliders();

            postponeStructureChanges();
            for (WorldObject worldObject : scene.getAllObjects()) {
                worldObject.fixedUpdate(lastFixedUpdateTime, fixedUpdateStepTime);
            }
            fixedDeltaTime = timeSec - lastFixedUpdateTime;
            runPostponedStructureChanges();
        }

        final float deltaTime = timeSec - lastUpdateTime;
        lastUpdateTime = timeSec;
        postponeStructureChanges();
        for (WorldObject worldObject : scene.getAllObjects()) {
            worldObject.update(timeSec);
        }
        runPostponedStructureChanges();

        for (System system : systems) {
            if (system instanceof UpdateSystem) {
                ((UpdateSystem) system).onUpdate(timeSec, deltaTime);
            }
        }
    }

    private void buildColliders() {
        colliders.clear();
        for (WorldObject object : scene.getAllObjects()) {
            if (object.canCollide()) {
                colliders.add(object);
            }
        }
    }

    public <T extends WorldObject.Component> T findFirstObjectWithComponent(Class<T> aClass) {
        for (WorldObject object : scene.getAllObjects()) {
            for (WorldObject.Component component : object.getComponents()) {
                if (aClass.isAssignableFrom(component.getClass())) {
                    return (T) component;
                }
            }
        }
        return null;
    }

    public static abstract class System {
        private World world;
        private void attach(World world) {
            this.world = world;
            onAttached();
        }
        protected final World world() {
            return world;
        }
        protected void onAttached() {
        }
    }

    public interface UpdateSystem {
        void onUpdate(float timeSec, float deltaTime);
    }

    public interface InputSystem {
        void onClick(WorldObject receiver, float x, float y);
    }
}
