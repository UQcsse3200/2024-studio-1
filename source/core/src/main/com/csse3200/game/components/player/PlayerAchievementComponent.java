package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;

public class PlayerAchievementComponent extends Component {
    public PlayerAchievementComponent() {
        super();
    }

    @Override
    public void create() {
        //Add events here maybe?
        entity.getEvents().addListener("updateSpeedPercentage", this::energyDrinkAchievement);
    }

    public void energyDrinkAchievement(float speedPercentage) {
        addAchievement(new Achievement("images/items/energy_drink_blue.png", "max speed"));
//        if (speedPercentage == entity.getComponent(PlayerActions.class).getMaxSpeed()) {
//            addAchievement(new Achievement("images/items/energy_drink_blue.png", "max speed"));
//        }

    }

    public void addAchievement(Achievement achievement) {
        //Check that achievement has not yet been won
        //Add to config
        String achievementName = achievement.getName();
        ServiceLocator.getAlertBoxService().
                showAlert("Achievement won!", String.format("Congratulations, you have achieved the %s achievement!", achievementName));
    }


}
