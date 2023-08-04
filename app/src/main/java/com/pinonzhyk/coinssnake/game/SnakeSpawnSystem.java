package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class SnakeSpawnSystem extends World.System implements World.UpdateSystem, World.InputSystem {

    private boolean snakeSpawned = false;
    private Snake spawned;

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        if (snakeSpawned) {
            return;
        }

        boolean worldContainFlower = false;
        for (WorldObject object : world().getObjects()) {
            if (object.hasComponent(Flower.class)) {
                worldContainFlower = true;
                break;
            }
        }

        if (worldContainFlower) {
            float startCoord = world().getBoundsWidthUnits() * 0.1f;
            WorldObject snake = new WorldObject(startCoord, startCoord);
            spawned = new Snake();
            snake.addComponent(spawned);
            world().instantiateWorldObject(snake);
            snakeSpawned = true;
        }
    }

    @Override
    public void onClick(WorldObject receiver, float x, float y) {
        if (receiver == null && spawned != null) {
            spawned.changeDirection();
        }
    }
}
