package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.TextComponent;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class EconomySystem extends World.System implements World.UpdateSystem {

    private int money;
    private TextComponent moneyText;

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

    @Override
    public void onUpdate(float timeSec, float deltaTime) {
        if (moneyText == null) {
            int x = (int) (world().getBoundsWidthUnits() * 0.8f);
            int y = (int) (world().getBoundsHeightUnits() * 0.075f);

            moneyText = new TextComponent();
            //2.5% of the horizontal space for 1 character
            moneyText.textSize = (int) (world().getBoundsWidthUnits() * 0.025f);
            final WorldObject moneyTextObj = new WorldObject(x, y);
            moneyTextObj.addComponent(moneyText);

            world().instantiateWorldObject(moneyTextObj);
        }

        if (moneyText != null) {
            moneyText.text = "Money: " + money;
        }
    }
}
