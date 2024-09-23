package com.csse3200.game.components.player;

public class Achievement {
    String icon;
    String name;
    public Achievement(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    /**
     * A method that returns the path to this achievement's icon
     * @return string representation of the path to this achievement's icon
     */
    public String getIcon() {
        return this.icon;
    }

    /**
     * A method that retrieves the name of this achievement
     * @return achievement name
     */
    public String getName() {
        return this.name;
    }

}
