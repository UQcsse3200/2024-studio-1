package com.csse3200.game.components.player;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.LoadPlayer;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.LoadedFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Loads and creates PlayerFactories.
 */
public class PlayerFactoryFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFactoryFactory.class);

    /**
     *  List of default configs. (as filenames)
      */
    private static final List<String> DEFAULT_PLAYER_OPTIONS = List.of(
            "player.json",
            "player_2.json",
            "player_3.json",
            "player_4.json",
            "bear.json",
            "necromancer.json"
    );

    /**
     * List of all Options this Factory can create.
     */
    private final Map<String, PlayerConfig> options;

    /**
     * Construct the Factory and load everything.
     *
     * @param options the list of options
     */
    private PlayerFactoryFactory(Map<String, PlayerConfig> options) {
        super();
        this.options = options;
        this.load();
        logger.info("Loaded {} players\n{}", options.size(), String.join("\n", options.keySet()));
    }

    /**
     * Construct the Factory from a list of configs
     * (this is useful for loading a saved player from a list of save files.)
     *
     * @param configs the configs for all the player options.
     */
    public PlayerFactoryFactory(List<PlayerConfig> configs) {
        this(configs.stream().collect(Collectors.toMap(value -> value.name, value -> value)));
    }

    /**
     * Construct the Factory from the default list of configs.
     */
    public PlayerFactoryFactory() {
        this(DEFAULT_PLAYER_OPTIONS.stream()
                     .map(s -> FileLoader.readClass(PlayerConfig.class, "configs/" + s))
                     .toList());
    }

    /**
     * Get all the options this Factory can build
     * (this is useful for creating a new player from the defaults.)
     *
     * @return a map of options listed by name.
     */
    public Map<String, PlayerConfig> getOptions() {
        return new HashMap<>(this.options);
    }

    /**
     * Concrete implementation of the Player Factory including Loading all assets.
     */
    private static class LoadedPlayerFactory extends LoadedFactory implements PlayerFactory {
        private final PlayerConfig config;

        /**
         * Create a Player factory from a config.
         *
         * @param config the config of the player factory to create.
         */
        public LoadedPlayerFactory(PlayerConfig config) {
            super(logger);
            this.config = config;
            this.load();
        }

        @Override
        public Entity create(GameOptions.Difficulty difficulty) {
            this.load();
            return new LoadPlayer().createPlayer(config.adjustForDifficulty(difficulty));
        }

        @Override
        protected String[] getTextureAtlasFilepaths() {
            if (this.config == null) {
                return super.getTextureAtlasFilepaths();
            }
            return new String[]{
                    this.config.textureAtlasFilename
            };
        }

        @Override
        protected String[] getTextureFilepaths() {
            if (this.config == null) {
                return super.getTextureAtlasFilepaths();
            }
            return new String[]{
                    PlayerStatsDisplay.DAMAGE_BUFF_TEXTURE,
                    PlayerStatsDisplay.SPEED_TEXTURE,
                    this.config.textureFilename
            };
        }

        @Override
        protected String[] getSoundFilepaths() {
            return new String[]{
                    "sounds/collectCoin.mp3",
                    "sounds/hit2.ogg"
            };
        }
    }

    /**
     * Create a Factory for the player with a given name.
     *
     * @param name the name fo the player to create.
     * @return a factory for the chosen player entity.
     */
    public PlayerFactory create(String name) {
        logger.info("Creating Factory for player: {}", name);
        return new LoadedPlayerFactory(options.get(name));
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        if (this.options == null) {
            return new String[]{};
        }
        return options.values().stream()
                       .map(config -> config.textureAtlasFilename)
                       .toArray(String[]::new);
    }

    @Override
    protected String[] getTextureFilepaths() {
        if (this.options == null) {
            return new String[]{};
        }
        return options.values().stream()
                       .map(config -> config.textureFilename)
                       .toArray(String[]::new);
    }
}
