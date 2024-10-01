package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.LoadPlayer;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;


/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
    Map<String, PlayerConfig> options;

    /**
     * Construct a new Player Factory (and load all of its assets)
     */
    public PlayerFactory(List<String> configFilenames) {
        super(logger);
        this.options = configFilenames.stream()
                .map(filename -> FileLoader.readClass(PlayerConfig.class, filename))
                .collect(Collectors.toMap(value -> value.name, value -> value));
        this.load(logger);
    }

    public Entity createPlay(String fileName, Difficulty difficulty) {
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = FileLoader.readClass(PlayerConfig.class, fileName);
        config.adjustForDifficulty(difficulty);
        return loader.createPlayer(config);
    }

    /**
     * Create a player entity
     *
     * @return entity
     */
    public Entity createPlayer(){
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = options.get("default");
        return loader.createPlayer(config);
    }

    /**
     * Create a player.
     * @param player the name of the player (name attribute).
     * @param difficulty difficulty chosen by the player, affects player attributes
     * @return the player entity.
     */
    public Entity createPlayer(String player, Difficulty difficulty) {
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = options.get(player);
        config.adjustForDifficulty(difficulty);
        return loader.createPlayer(config);
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        if (this.options == null){
            return new String[]{};
        }
        return options.values().stream().map(config -> config.textureAtlasFilename).toArray(String[]::new);
    }

    @Override
    protected String[] getTextureFilepaths() {
        if (this.options == null){
            return new String[]{};
        }
        List<String> result = new ArrayList<>(options.values().stream().map(config -> config.textureFilename).toList());
        result.add(PlayerStatsDisplay.DAMAGE_BUFF_TEXTURE);
        result.add(PlayerStatsDisplay.SPEED_TEXTURE);
        return result.toArray(String[]::new);
//        return options.values().stream().map(config -> config.textureFilename).toArray(String[]::new);
    }
}

