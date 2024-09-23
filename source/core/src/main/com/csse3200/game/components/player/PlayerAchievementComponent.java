package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;

public class PlayerAchievementComponent extends Component {
    public PlayerAchievementComponent() {
        super();
    }

    @Override
    public void create() {
        //Add events here maybe?
        entity.getEvents().addListener("energyDrinkAchievement", this::addAchievement);
    }

    public void addAchievement(Achievement achievement) {
        //Add to config
    }


}
