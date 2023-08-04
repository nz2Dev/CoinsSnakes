package com.pinonzhyk.coinssnake.world;

import java.util.ArrayList;
import java.util.Collection;
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
    private final List<Component> components = new ArrayList<>();
    private World world;

    public WorldObject(int x, int y) {
        this.position = new IntVector2(x, y);
        this.inputSurfaceSize = new IntVector2(0, 0);
    }

    public WorldObject(int x, int y, int surfaceSizeX, int surfaceSizeY) {
        this.position = new IntVector2(x, y);
        this.inputSurfaceSize = new IntVector2(surfaceSizeX, surfaceSizeY);
    }

    public void addComponent(Component component) {
        if (components.contains(component)) {
            throw new RuntimeException("component already exist: " + component);
        }
        components.add(component);
        component.attach(this);
    }

    public <T extends Component> boolean hasComponent(Class<T> type) {
        // todo optimize using hashMap
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return true;
            }
        }
        return false;
    }

    public Collection<Component> getComponents() {
        // todo should return some read-only collection
        return components;
    }

    protected void init(World world) {
        this.world = world;
        for (Component component : components) {
            component.init();
        }
    }

    protected void update(float timeSec) {
        for (Component component : components) {
            if (component instanceof UpdateReceiver) {
                ((UpdateReceiver) component).onUpdate(timeSec);
            }
        }
    }

    protected void receiveClickEvent(int x, int y) {
        for (Component component : components) {
            if (component instanceof ClickEventReceiver) {
                ClickEventReceiver receiver = (ClickEventReceiver) component;
                receiver.onClickEvent(x, y);
            }
        }
    }

    public void fixedUpdate(float fixedUpdateTime, float fixedUpdateStepTime) {
        for (Component component : components) {
            if (component instanceof FixedTimeUpdateReceiver) {
                ((FixedTimeUpdateReceiver) component).onFixedUpdate(fixedUpdateTime, fixedUpdateStepTime);
            }
        }
    }

    public static class Component {
        private WorldObject object;

        private void attach(WorldObject holder) {
            this.object = holder;
        }

        protected WorldObject object() {
            return object;
        }
        protected World world() {
            return object.world;
        }

        private void init() {
            onInit();
        }

        protected void onInit() {
        }
    }

    public interface UpdateReceiver {
        void onUpdate(float timeSec);
    }

    public interface FixedTimeUpdateReceiver {
        void onFixedUpdate(float fixedTimeSec, float deltaTimeStep);
    }

    public interface ClickEventReceiver {
        void onClickEvent(int x, int y);
    }
}
