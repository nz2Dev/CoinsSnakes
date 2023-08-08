package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.GraphicComponent;
import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class FlowerPlantSystem extends World.System implements World.InputSystem {

    private final int flowerPrice;

    public FlowerPlantSystem(int flowerPrice) {
        this.flowerPrice = flowerPrice;
    }

    @Override
    public void onClick(WorldObject receiver, float x, float y) {
        final EconomySystem economySystem = world().findSystem(EconomySystem.class);
        if (receiver == null) {
            if (economySystem.canAfford(flowerPrice)) {
                economySystem.spend(flowerPrice);

                final float size = world().getBoundsWidthUnits() * 0.1f;
                WorldObject flower = new WorldObject(x, y, new Vector2(size, size), new Vector2(size, size));
                flower.setTagId(GameManager.FLOWER_TAG_ID);;
                flower.addComponent(new GraphicComponent(size));
                flower.addComponent(new Flower());
                world().instantiateWorldObject(flower);
                world().findSystem(GameManager.class).flowerPlanted();
            }
        }
    }

}
