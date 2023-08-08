package com.pinonzhyk.coinssnake.world;

public class TextComponent extends WorldObject.Component {

    public String text;
    public float textSize;
    public boolean centered;

    public TextComponent() {
        textSize = 1;
    }

    public TextComponent(String text, float textSize, boolean centered) {
        this.text = text;
        this.textSize = textSize;
        this.centered = centered;
    }
}
