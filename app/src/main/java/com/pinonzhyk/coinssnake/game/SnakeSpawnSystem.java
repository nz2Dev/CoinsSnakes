package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class SnakeSpawnSystem extends World.System implements World.UpdateSystem {

    private float lastSpawnTime;
    private float spawnInterval = 5f;
    private float lastSpawningFrequencyCheckTime = -1;
    private float spawningFrequencyCheckInterval = 10;

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        if (lastSpawningFrequencyCheckTime < 0) {
            lastSpawningFrequencyCheckTime = timeSec;
        }
        if (lastSpawningFrequencyCheckTime + spawningFrequencyCheckInterval < timeSec) {
            lastSpawningFrequencyCheckTime = timeSec;
            spawnInterval -= spawnInterval * 0.05f;
        }

        final GameManager gameManager = world().findSystem(GameManager.class);
        if (!gameManager.isSnakeSpawnAllowed() || lastSpawnTime + spawnInterval > timeSec) {
            return;
        }

        lastSpawnTime = timeSec;
        float startCoord = (float) (world().getBoundsWidthUnits() * (Math.random() * 0.8 + 0.1));
        WorldObject snake = new WorldObject(startCoord, startCoord);
        snake.addComponent(new Snake());
        world().instantiateWorldObject(snake);
    }
}
