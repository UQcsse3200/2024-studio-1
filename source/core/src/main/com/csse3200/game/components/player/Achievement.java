package com.csse3200.game.components.player;

public class Achievement {
    String icon;
    String name;
    public Achievement(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

}
