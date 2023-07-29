package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class GameManager {

    private final World gameWorld;

    public GameManager() {
        gameWorld = new World(1000, 1000);
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public void createGame() {
        final WorldObject movableObj = new WorldObject(25, 25, 100, 100);
        movableObj.addLogicComponent(new Movable());
        gameWorld.instantiateWorldObject(movableObj);

        gameWorld.setClickEventsListener((receiver, x, y) -> {
            if (receiver == null) {
                gameWorld.instantiateWorldObject(new WorldObject(x, y, 50, 50));
            }
        });
    }

}
