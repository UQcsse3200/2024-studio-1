package com.csse3200.game.components.player;


import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.AchievementConfig;
import com.csse3200.game.files.FileLoader;

import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;



public class PlayerAchievementComponent extends Component {
    public AchievementConfig config = new AchievementConfig();
    public HashMap<String, String> achievements;
    public PlayerAchievementComponent() {
        super();
        FileLoader.readClass(config.getClass(), "configs/achievements.json");
        System.out.println("read");
        achievements = new HashMap<>();
    }

    public AchievementConfig getConfig() {
        return config;
    }

    @Override
    public void create() {
        // Load previously gained achievements
        // this.config = FileLoader.readClass(AchievementConfig.class, "configs/achievements.json");
        //Add event handling
        entity.getEvents().addListener("updateSpeedPercentage", this::energyDrinkAchievement);
    }

    /**
     * A method to handle whether an energy drink achievement has been obtained. It is called any time an energy drink
     * is used.
     * @param speedPercentage
     */
    public void energyDrinkAchievement(float speedPercentage) {
        addAchievement("First energy drink", "images/items/energy_drink_blue.png");
        // addAchievement(new Achievement("First energy drink", "images/items/energy_drink_blue.png"));
    }

    /**
     * Adds the (name, icon) pair of an achievement to the inner achievement list of the AchievementConfig file
     */
    public void addAchievement(String name, String icon) {
        /*
        achievements.put(name, icon);
        ServiceLocator.getAlertBoxService().
                showAlert("Achievement won!",
                        String.format("Congratulations, you have achieved the %s achievement!", name));


         */
        //There's an issue with the below code - says that this.config is null?
        //Check that this achievement is not already in the map
        if (!(this.config.achievements.containsKey(name))){
            //Add achievement to the inner list
            this.config.achievements.put(name, icon);
            for (String entry : this.config.achievements.keySet())
                System.out.println(entry);
            //Send an alert to the player
            ServiceLocator.getAlertBoxService().
                    showAlert("Achievement won!", String.format("Congratulations, you have achieved the %s achievement!", name));
            updateConfig();
        }
    }

    /**
     * I'm not sure about this one? Do we need to be constantly updating the AchievementConfig file?
     */
    public void updateConfig() {
        FileLoader.writeClass(this.config, "configs/achievements.json", FileLoader.Location.LOCAL);
    }


}
