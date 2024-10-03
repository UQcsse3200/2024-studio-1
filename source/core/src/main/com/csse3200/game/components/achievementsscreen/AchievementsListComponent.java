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

    /**
     * Reads player achievements list from external file.
     */
    public AchievementsListComponent() {
        this(PlayerAchievementComponent.ACHIEVEMENT_FILE, Location.EXTERNAL);
    }

    /**
     * Reads player achievements list from a file.
     * @param path achievements file path.
     * @param location file location.
     */
    public AchievementsListComponent(String path, Location location) {
        achievements = FileLoader.readClass(HashMap.class, path, location);
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
        return achievements == null ? 0 : achievements.size();
    }
}
