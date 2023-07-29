package com.pinonzhyk.coinssnake.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Act as atomic game object which is both authoring and read-only state representation
 * of the state of the world
 */
public class WorldObject {

    public final IntVector2 position;
    private final List<LogicComponent> logicComponents = new ArrayList<>();

    public WorldObject(int x, int y) {
        this.position = new IntVector2(x, y);
    }

    public void addLogicComponent(LogicComponent logicComponent) {
        if (logicComponents.contains(logicComponent)) {
            throw new RuntimeException("component already exist: " + logicComponent);
        }
        logicComponents.add(logicComponent);
        logicComponent.attach(this);
    }

    protected void update(float timeSec) {
        for (LogicComponent logicComponent : logicComponents) {
            logicComponent.update(timeSec);
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

        private void update(float timeSec) {
            onUpdate(timeSec);
        }

        protected abstract void onUpdate(float timeSec);
    }
}
