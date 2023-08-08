package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.TextComponent;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class PlayerDisplaySystem extends World.System {
    private String playerName;

    public PlayerDisplaySystem(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected void onAttached() {
        final float x = world().getBoundsWidthUnits() * 0.5f;
        final float y = world().getBoundsWidthUnits() * 0.075f;
        final WorldObject playerNameObject = new WorldObject(x, y);
        playerNameObject.addComponent(new TextComponent(playerName, world().getBoundsWidthUnits() * 0.025f, true));
        world().instantiateWorldObject(playerNameObject);
    }

}
