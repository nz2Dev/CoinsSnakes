package com.pinonzhyk.coinssnake.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Act as atomic game object which is both authoring and read-only state representation
 * of the state of the world
 */
public class WorldObject {

    /**
     * defines objects surface for input raycast events.
     * set to non zero, to receive onClick event.
     * center point is half of the vector size.
     * Can't be negative.
     */
    public final IntVector2 inputSurfaceSize ;
    public final IntVector2 position;
    private final List<LogicComponent> logicComponents = new ArrayList<>();

    public WorldObject(int x, int y) {
        this.position = new IntVector2(x, y);
        this.inputSurfaceSize = new IntVector2(0, 0);
    }

    public WorldObject(int x, int y, int surfaceSizeX, int surfaceSizeY) {
        this.position = new IntVector2(x, y);
        this.inputSurfaceSize = new IntVector2(surfaceSizeX, surfaceSizeY);
    }

    public void addLogicComponent(LogicComponent logicComponent) {
        if (logicComponents.contains(logicComponent)) {
            throw new RuntimeException("component already exist: " + logicComponent);
        }
        logicComponents.add(logicComponent);
        logicComponent.attach(this);
    }

    public <T extends LogicComponent> boolean hasLogicComponent(Class<T> type) {
        // todo optimize using hashMap
        for (LogicComponent logicComponent : logicComponents) {
            if (type.isAssignableFrom(logicComponent.getClass())) {
                return true;
            }
        }
        return false;
    }

    protected void init() {
        for (LogicComponent logicComponent : logicComponents) {
            logicComponent.init();
        }
    }

    protected void update(float timeSec) {
        for (LogicComponent logicComponent : logicComponents) {
            logicComponent.update(timeSec);
        }
    }

    protected void receiveClickEvent(int x, int y) {
        for (LogicComponent logicComponent : logicComponents) {
            if (logicComponent instanceof ClickEventReceiver) {
                ClickEventReceiver receiver = (ClickEventReceiver) logicComponent;
                receiver.onClickEvent(x, y);
            }
        }
    }

    public static abstract class LogicComponent {

        private WorldObject object;

        private void attach(WorldObject holder) {
            this.object = holder;
        }

        protected WorldObject object() {
            return object;
        }

        private void init() {
            onInit();
        }

        protected void onInit() {
        }

        private void update(float timeSec) {
            onUpdate(timeSec);
        }

        protected abstract void onUpdate(float timeSec);
    }

    public interface ClickEventReceiver {
        void onClickEvent(int x, int y);
    }
}
