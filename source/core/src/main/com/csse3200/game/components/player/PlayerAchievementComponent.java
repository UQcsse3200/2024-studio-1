package com.csse3200.game.components.player;


import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.AchievementConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;

import java.util.Map;


public class PlayerAchievementComponent extends Component {
    AchievementConfig config;
    public PlayerAchievementComponent() {
        super();
    }

    @Override
    public void create() {
        //Load in the config file
        this.config = FileLoader.readClass(AchievementConfig.class, "configs/achievements.json");

        //Add event handling
        entity.getEvents().addListener("updateSpeedPercentage", this::energyDrinkAchievement);
    }

    /**
     * A method to handle whether an energy drink achievement has been obtained. It is called any time an energy drink
     * is used.
     * @param speedPercentage
     */
    public void energyDrinkAchievement(float speedPercentage) {
        addAchievement(new Achievement("First energy drink", "images/items/energy_drink_blue.png"));
    }

    /**
     * Adds the (name, icon) pair of an achievement to the inner achievement list of the AchievementConfig file
     * @param achievement a class that stores the name and icon of an achievement
     */
    public void addAchievement(Achievement achievement) {
        //
        ServiceLocator.getAlertBoxService().
                showAlert("Achievement won!", String.format("Congratulations, you have achieved the %s achievement!", achievement.getName()));

        //There's an issue with the below code - says that this.config is null?
//        //Check that this achievement is not already in the map
//        if (!(this.config.achievements.containsKey(achievement.getName()))){
//            //Add achievement to the inner list
//            this.config.achievements.put(achievement.getName(), achievement.getIcon());
//            //Send an alert to the player
//            ServiceLocator.getAlertBoxService().
//                    showAlert("Achievement won!", String.format("Congratulations, you have achieved the %s achievement!", achievement.getName()));
//            updateConfig();
//        }
    }

    /**
     * I'm not sure about this one? Do we need to be constantly updating the AchievementConfig file?
     */
    public void updateConfig() {
        //Update the config file?
        FileLoader.writeClass(this.config, "configs/achievements.json");
    }


}
