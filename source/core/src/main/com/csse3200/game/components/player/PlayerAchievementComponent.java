package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;

public class PlayerAchievementComponent extends Component {
    public PlayerAchievementComponent() {
        super();
    }

    @Override
    public void create() {
        //Add events here maybe?
        entity.getEvents().addListener("updateSpeedPercentage", this::energyDrinkAchievement);
    }

    public void energyDrinkAchievement() {
        if (entity.getComponent(PlayerActions.class).getCurrSpeedPercentage() == entity.getComponent(PlayerActions.class).getMaxSpeed()) {
            addAchievement(new Achievement("images/items/energy_drink_blue.png", "Max speed achievement"));
        }

    }

    public void addAchievement(Achievement achievement) {
        //Add to config
    }


}
