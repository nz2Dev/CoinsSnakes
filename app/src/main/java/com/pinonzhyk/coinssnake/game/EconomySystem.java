package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.World;

public class EconomySystem extends World.System {

    private int money;

    public EconomySystem(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public boolean canAfford(int price) {
        return money >= price;
    }

    public void spend(int money) {
        this.money -= money;
    }

}
