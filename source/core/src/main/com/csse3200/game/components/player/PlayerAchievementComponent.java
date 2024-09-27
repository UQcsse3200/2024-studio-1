package com.csse3200.game.components.player;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.files.FileLoader;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ui.UIComponent;
import java.util.HashMap;



public class PlayerAchievementComponent extends UIComponent {
    public HashMap<String, String> achievements;
    public PlayerAchievementComponent() {
        if (FileLoader.readClass(HashMap.class, "configs/achievements.json",
                FileLoader.Location.EXTERNAL) == null) {
            achievements = FileLoader.readClass(HashMap.class, "configs/achievements.json",
                    FileLoader.Location.LOCAL);
        }
        else {
            achievements = FileLoader.readClass(HashMap.class, "configs/achievements.json",
                    FileLoader.Location.EXTERNAL);
        }

    }


    /**
     * Gets the map of achievements
     * @return
     */
    public HashMap<String, String> getAchievements() {
        return achievements;
    }

    @Override
    public void create() {
        super.create();

        //Add event handling
        entity.getEvents().addListener("updateSpeedPercentage", this::energyDrinkAchievement);
        entity.getEvents().addListener("defeatedEnemy", this::defeatedEnemy);
        entity.getEvents().addListener("defeated10", this::defeated10);
    }


    @Override
    protected void draw(SpriteBatch batch) {
        // handled by superclass
    }

    public void defeatedEnemy() {
        addAchievement("First animal defeated", "images/npc/birdman.png");
    }
    public void defeated10() {
        addAchievement("Defeated 10 Animals", "images/npc/birdman.png");
    }

    /**
     * A method to handle whether an energy drink achievement has been obtained. It is called any time an energy drink
     */
    public void energyDrinkAchievement(float speedPercentage) {
        addAchievement("First energy drink", "images/items/energy_drink_blue.png");
    }

    /**
     * Adds the name and the icon of an achievement to a hashMap and
     * display it on UI
     *
     * @param name name of the achievement
     * @param icon path to image file
     *
     */
    public void addAchievement(String name, String icon) {

        if (!this.achievements.containsKey(name)){
            //Add achievement to the inner list
            this.achievements.put(name, icon);
            // Create a Label to display the achievement message
            String text = String.format("Congratulations, you have achieved the '%s' achievement!", name);
            Label achievementLabel = new Label(text, skin, "small");
            Table table = new Table();
            table.center();
            table.setFillParent(true);
            table.add(achievementLabel).padTop(5f);
            stage.addActor(table);
            // unrender the label after 1 second of display
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    table.remove(); // Remove the label from the screen
                }
            }, 1);
            updateConfig();
        }
    }

    /**
     * Writes the Hashmap to Json file
     */
    public void updateConfig() {
        FileLoader.writeClass(achievements, "configs/achievements.json", FileLoader.Location.EXTERNAL);
    }

}
