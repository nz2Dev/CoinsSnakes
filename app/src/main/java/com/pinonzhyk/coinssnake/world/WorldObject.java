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
    public final Vector2 inputSurfaceSize;
    public final Vector2 colliderSurfaceSize;
    public final Vector2 position;
    private final List<Component> components = new ArrayList<>();
    private World world;
    private int tagId;

    public WorldObject(float x, float y) {
        this.position = new Vector2(x, y);
        this.inputSurfaceSize = new Vector2();
        this.colliderSurfaceSize = new Vector2();
        this.tagId = -1;
    }

    public WorldObject(float x, float y,
                        Vector2 inputSurfaceSize,
                        Vector2 colliderSurfaceSize) {
        this.position = new Vector2(x, y);
        this.inputSurfaceSize = inputSurfaceSize == null ? new Vector2() : inputSurfaceSize;
        this.colliderSurfaceSize = colliderSurfaceSize == null ? new Vector2() : colliderSurfaceSize;
        this.tagId = -1;
    }

    public void setTagId(int tagId) {
        if (tagId < 0) {
            throw new RuntimeException("can't set negative tag id, it's reserved for special cases");
        }
        this.tagId = tagId;
    }

    public int getTagId() {
        return tagId;
    }

    protected boolean canCollide() {
        return !colliderSurfaceSize.isZero();
    }

    protected boolean canBeClicked() {
        return !inputSurfaceSize.isZero();
    }

    public void addComponent(Component component) {
        if (components.contains(component)) {
            throw new RuntimeException("component already exist: " + component);
        }
        components.add(component);
        component.attach(this);
    }

    public <T extends Component> T findComponent(Class<T> type) {
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return (T) component;
            }
        }
        return null;
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

    protected void receiveClickEvent(float x, float y) {
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

    public void destroy() {
        for (Component component : components) {
            component.destroy();
        }
    }

    public static class Component {
        private WorldObject object;

        private void attach(WorldObject holder) {
            this.object = holder;
        }

        public WorldObject object() {
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

        private void destroy() {
            onDestroy();
        }

        protected void onDestroy() {
        }
    }

    public interface UpdateReceiver {
        void onUpdate(float timeSec);
    }

    public interface FixedTimeUpdateReceiver {
        void onFixedUpdate(float fixedTimeSec, float deltaTimeStep);
    }

    public interface ClickEventReceiver {
        void onClickEvent(float x, float y);
    }
}
