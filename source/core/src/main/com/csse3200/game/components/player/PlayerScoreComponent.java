package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.player.inventory.CoinsComponent;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;

public class PlayerScoreComponent extends UIComponent {

    /**
     * Contains the name and the image of achievements collected
     */
    public HashMap<String, Integer> scores;
    private int enemyKillCounts;
    private int totalCoinsEarned;
    public static final String SCORE_FILE = "configs/scores.json";

    /**
     * Constructs an Player Score  component to an Entity
     */
    public PlayerScoreComponent() {
        // Initialize scores from JSON file
        scores = new HashMap<>();
        scores.put("coins",totalCoinsEarned);
        scores.put("kills",enemyKillCounts);
        enemyKillCounts = 0; // Initialize kill count
        totalCoinsEarned = 0; // Initialize coins earned
    }

    /**
     * Gets the map of scores
     * @return a map containing the scores
     */
    public HashMap<String, Integer> getScores() {
        return scores;
    }

    /**
     * Called upon creation of this component
     */
    public void create() {
        super.create();
        // Add event handling
        entity.getEvents().addListener("defeatedEnemy", this::handleDefeatedEnemy);
        entity.getEvents().addListener("updateCoins", this::handleCoinsGained);
    }

    /**
     * Method inherited from superclass
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // handled by superclass
    }

    /**
     * Handles scores for when an enemy is defeated.
     * @param deathCount the total number of enemies defeated
     */
    public void handleDefeatedEnemy(int deathCount) {
        enemyKillCounts = deathCount;// Update enemy kill count with the total death count
        updateConfig();
    }

    /**
     * Handles scores for coins collected throughout a game
     */
    public void handleCoinsGained() {
        totalCoinsEarned+= entity.getComponent(CoinsComponent.class).getCoins();
        updateConfig();
    }


    /**
     * Resets the list of scores to an empty list
     */
    public void resetScores() {
        scores = new HashMap<>();
        FileLoader.writeClass(scores, SCORE_FILE, FileLoader.Location.EXTERNAL);
    }

    /**
     * Writes the HashMap to JSON file
     */
    public void updateConfig() {
        scores.put("coins",totalCoinsEarned);
        scores.put("kills",enemyKillCounts);
        FileLoader.writeClass(scores, SCORE_FILE, FileLoader.Location.EXTERNAL);
    }
}
