package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.PlayerHealthDisplay;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.LoadPlayer;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);

    private final Map<String, PlayerConfig> options;

    /**
     * Construct a new Player Factory (and load all of its assets)
     */
    public PlayerFactory(List<String> configFilenames) {
        super(logger);
        this.options = configFilenames.stream()
                               .map(filename -> {
                                   PlayerConfig config = FileLoader.readClass(PlayerConfig.class, filename);
                                   if (config == null) {
                                       throw new IllegalArgumentException("Could not load config file " + filename);
                                   }
                                   return config;
                               })
                               .collect(Collectors.toMap(value -> value.name, value -> value));
        this.load(logger);
    }

    /**
     * Create a player entity
     *
     * @return entity
     */
    public Entity createPlayer() {
        LoadPlayer loader = new LoadPlayer();
        PlayerConfig config = options.get("default");

        InventoryComponent inventoryComponent = new InventoryComponent();

        Entity player = new Entity()


                .addComponent(inventoryComponent)
                .addComponent(new PlayerStatsDisplay())
                .addComponent(new PlayerInventoryDisplay(inventoryComponent))
                .addComponent(new PlayerHealthDisplay());
        return loader.createPlayer(config);
    }


    /**
     * Create a player.
     *
     * @param config     The configuration for the player to create.
     * @param difficulty difficulty chosen by the player, affects player attributes
     * @return the player entity.
     */
    public Entity createPlayer(PlayerConfig config, Difficulty difficulty) {
        LoadPlayer loader = new LoadPlayer();
        config.adjustForDifficulty(difficulty);
        return loader.createPlayer(config);
    }

    /**
     * Create a player.
     *
     * @param name       the name of the default character to create.
     * @param difficulty difficulty chosen by the player, affects player attributes
     * @return the player entity.
     */
    public Entity createPlayer(String name, Difficulty difficulty) {
        return createPlayer(options.get(name), difficulty);
    }

    /**
     * Get the list of default options.
     *
     * @return a map of all the player configs by name.
     */
    public Map<String, PlayerConfig> getOptions() {
        return new HashMap<>(options);
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        if (this.options == null) {
            return new String[]{};
        }
        return options.values().stream().map(config -> config.textureAtlasFilename).toArray(String[]::new);
    }

    @Override
    protected String[] getTextureFilepaths() {
        if (this.options == null) {
            return new String[]{};
        }
        List<String> result = new ArrayList<>(options.values().stream().map(config -> config.textureFilename).toList());
        result.add(PlayerStatsDisplay.DAMAGE_BUFF_TEXTURE);
        result.add(PlayerStatsDisplay.SPEED_TEXTURE);
        return result.toArray(String[]::new);
//        return options.values().stream().map(config -> config.textureFilename).toArray(String[]::new);
    }
}
