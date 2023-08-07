package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class SnakeSpawnSystem extends World.System implements World.UpdateSystem {

    private float lastSpawnTime;
    private float spawnInterval = 5f;

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        boolean worldContainFlower = false;
        for (WorldObject object : world().getObjects()) {
            if (object.hasComponent(Flower.class)) {
                worldContainFlower = true;
                break;
            }
        }

        if (!worldContainFlower) {
            return;
        }
        if (lastSpawnTime + spawnInterval > timeSec) {
            return;
        }

        lastSpawnTime = timeSec;
        float startCoord = (float) (world().getBoundsWidthUnits() * (Math.random() * 0.8 + 0.1));
        WorldObject snake = new WorldObject(startCoord, startCoord);
        snake.addComponent(new Snake());
        world().instantiateWorldObject(snake);
    }
}
