package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class SnakeSpawnSystem extends World.System implements World.UpdateSystem {

    private boolean snakeSpawned = false;

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        if (snakeSpawned) {
            return;
        }

        boolean worldContainFlower = false;
        for (WorldObject object : world().getObjects()) {
            if (object.hasLogicComponent(Flower.class)) {
                worldContainFlower = true;
                break;
            }
        }

        if (worldContainFlower) {
            WorldObject snake = new WorldObject(25, 25, 100, 50);
            snake.addLogicComponent(new Movable());
            world().instantiateWorldObject(snake);
            snakeSpawned = true;
        }
    }
}
