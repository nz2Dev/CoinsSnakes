package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class FlowerPlantSystem extends World.System implements World.InputSystem {

    private final int flowerPrice;

    public FlowerPlantSystem(int flowerPrice) {
        this.flowerPrice = flowerPrice;
    }

    @Override
    public void onClick(WorldObject receiver, int x, int y) {
        if (receiver == null) {
            final EconomySystem economySystem = world().findSystem(EconomySystem.class);
            if (economySystem.canAfford(flowerPrice)) {
                economySystem.spend(flowerPrice);

                WorldObject flower = new WorldObject(x, y, 50, 50);
                flower.addLogicComponent(new Flower());
                world().instantiateWorldObject(flower);
            }
        }
    }

}
