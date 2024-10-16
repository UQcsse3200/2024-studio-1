package com.csse3200.game.components.scorescreen;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.PlayerScoreComponent;
import com.csse3200.game.files.FileLoader;

import java.util.HashMap;

public class ScoreListComponent extends Component{

    private final HashMap<String,Integer> score;

    /**
     * Reads player scores list from external file.
     */
    public ScoreListComponent() {
        this(PlayerScoreComponent.SCORE_FILE, FileLoader.Location.EXTERNAL);
    }

    /**
     * Reads player achievements list from a file.
     * @param path achievements file path.
     * @param location file location.
     */
    public ScoreListComponent(String path, FileLoader.Location location) {
        score = FileLoader.readClass(HashMap.class, path, location);
    }

    /**
     * Gets the list of player scores.
     * @return the HashMap of player scoress.
     */
    public HashMap<String, Integer> getScore() {
        return score;
    }
}

