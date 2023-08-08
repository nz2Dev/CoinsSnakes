package com.pinonzhyk.coinssnake.game;

import android.util.Log;

import com.pinonzhyk.coinssnake.world.World;

public class GameManager extends World.System implements World.UpdateSystem {

    private int plantedFlowers;
    private int eatenFlowers;
    private float lastEatenFlowerTime;
    private float speedMultiplier;
    private final float eatenFlowerEffectDuration;

    public GameManager() {
        eatenFlowerEffectDuration = 5f;

        speedMultiplier = 1f;
    }

    boolean isSnakeSpawnAllowed() {
        return plantedFlowers > 0;
    }

    int maxSnakeSize() {
        return plantedFlowers >= 2 ? 10 : 5;
    }

    public float getSnakeSpeedMultiplier() {
        return speedMultiplier;
    }

    void flowerPlanted() {
        plantedFlowers++;
    }

    void flowerEaten() {
        eatenFlowers++;
    }

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        if (eatenFlowers > 0) {
            lastEatenFlowerTime = timeSec;
            speedMultiplier = 1f;
            plantedFlowers -= eatenFlowers;
            eatenFlowers = 0;
        }

        if (lastEatenFlowerTime + eatenFlowerEffectDuration > timeSec) {
            speedMultiplier = Math.min(speedMultiplier + deltaTime * 0.2f, 2f);
        } else {
            speedMultiplier = Math.max(speedMultiplier - deltaTime * 0.2f, 1f);
        }
    }
}
