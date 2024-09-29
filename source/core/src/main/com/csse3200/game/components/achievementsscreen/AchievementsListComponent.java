package com.csse3200.game.components.achievementsscreen;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.PlayerAchievementComponent;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.files.FileLoader.Location;

import java.util.HashMap;

/**
 * Reads player achievements list from external file, creates list as a HashMap.
 */
public class AchievementsListComponent extends Component {

    private final HashMap<String, String> achievements;
    private final int numAchievements;

    /**
     * Reads player achievements list from external file.
     */
    public AchievementsListComponent() {
        achievements = FileLoader.readClass(
                HashMap.class, PlayerAchievementComponent.ACHIEVEMENTS_PATH, Location.EXTERNAL);
        if (achievements == null) {
            numAchievements = 0;
        } else {
            numAchievements = achievements.size();
        }
    }

    /**
     * Gets the list of player achievements.
     * @return the HashMap of player achievements.
     */
    public HashMap<String, String> getAchievements() {
        return achievements;
    }

    /**
     * Gets the number of achievements.
     * @return the number of achievements or 0 if the achievements file could not be read/found.
     */
    public int getNumAchievements() {
        return numAchievements;
    }
}
