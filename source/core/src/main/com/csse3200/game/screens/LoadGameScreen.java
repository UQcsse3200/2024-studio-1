package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.components.player.PlayerFactoryFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.files.FileLoader;

import java.util.List;

import static com.csse3200.game.areas.MainGameArea.MAP_SAVE_PATH;

/**
 * The game screen loading the game
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class LoadGameScreen extends GameScreen {
    public LoadGameScreen(GdxGame game) {
        super(game);
        shouldLoad = true;
        PlayerConfig config = FileLoader.readClass(
                PlayerConfig.class,
                "saves/player_save.json",
                FileLoader.Location.EXTERNAL);
        if (config == null) {
            throw new RuntimeException("Tried to load player and failed");
        }
        PlayerFactoryFactory playerSelection = new PlayerFactoryFactory(List.of(config));
        Entity player = playerSelection.create(config.name).create(game.gameOptions.difficulty);
        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising load game screen entities");
        MapLoadConfig mapLoadConfig = FileLoader.readClass(MapLoadConfig.class, MAP_SAVE_PATH, FileLoader.Location.EXTERNAL);
        LevelFactory levelFactory = new MainGameLevelFactory(shouldLoad, mapLoadConfig);
        new MainGameArea(levelFactory, player, shouldLoad, mapLoadConfig);
        spawnPets(player, config);
    }

    private void spawnPets(Entity player, PlayerConfig config) {
        if (config.pets != null) {
            CollectibleFactory collectibleFactory = new CollectibleFactory();
            for (String petName : config.pets) {
                Collectible pet = collectibleFactory.create(petName);
                player.getComponent(InventoryComponent.class).pickup(pet);
            }
        }
    }
}

