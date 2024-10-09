package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.*;

import java.util.Arrays;

import static com.csse3200.game.areas.MainGameArea.MAP_SAVE_PATH;
import static com.csse3200.game.entities.PlayerSelection.PLAYERS;

/**
 * The game screen loading the game  
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class LoadGameScreen extends GameScreen{
    public LoadGameScreen(GdxGame game) {
        super(game);
        shouldLoad = true;
        /*
         * based on the characters selected, changed the link
         * If Player choose Load, then create
         */
        this.playerFactory = new PlayerFactory(Arrays.stream(PLAYERS).toList());
        PlayerConfig config = FileLoader.readClass(
                PlayerConfig.class,
                "saves/player_save.json",
                FileLoader.Location.EXTERNAL);
        if (config == null) {
            throw new RuntimeException("Tried to load player and failed");
        }
        Entity player = playerFactory.createPlayer(config, shouldLoad);
        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising load game screen entities");
        MapLoadConfig mapLoadConfig  = FileLoader.readClass(MapLoadConfig.class, MAP_SAVE_PATH, FileLoader.Location.EXTERNAL);
        LevelFactory levelFactory = new MainGameLevelFactory(shouldLoad, mapLoadConfig);
        new MainGameArea(levelFactory, player, shouldLoad, mapLoadConfig);
        spawnPets(player, config);
    }

    private void spawnPets(Entity player, PlayerConfig config){
      if (config.pets != null) {
            PetFactory petFactory = new PetFactory();
            for (String petName : config.pets) {
                Entity pet = petFactory.create(petName);
                player.getComponent(InventoryComponent.class).getInventory().addPet(pet);
                ServiceLocator.getEntityService().register(pet);
                pet.setPosition(5,7);
            }
        }

    }
}
