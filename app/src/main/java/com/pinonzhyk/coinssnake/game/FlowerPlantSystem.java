package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class FlowerPlantSystem extends World.System implements World.InputSystem {

    private final int flowerPrice;

    public FlowerPlantSystem(int flowerPrice) {
        this.flowerPrice = flowerPrice;
    }

    @Override
    public void onClick(WorldObject receiver, float x, float y) {
        if (receiver == null) {
            final EconomySystem economySystem = world().findSystem(EconomySystem.class);
            if (economySystem.canAfford(flowerPrice)) {
                economySystem.spend(flowerPrice);

                final float size = world().getBoundsWidthUnits() * 0.1f;
                WorldObject flower = new WorldObject(x, y, size, size);
                flower.addComponent(new Flower());
                world().instantiateWorldObject(flower);
            }
        }
    }

}
