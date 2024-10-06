package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.options.GameOptions.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.csse3200.game.GdxGame.ScreenType.LOSE;
import static com.csse3200.game.areas.GameController.MAP_SAVE_PATH;
import static com.csse3200.game.entities.PlayerSelection.PLAYERS;
import static com.csse3200.game.options.GameOptions.Difficulty.TEST;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class MainGameScreen extends GameScreen{

    public MainGameScreen(GdxGame game) {
        super(game);
        shouldLoad = false;
        GameArea gameArea = new GameArea();

        GameOptions gameOptions = game.gameOptions;
        logger.info("Starting game with difficulty {}", gameOptions.difficulty.toString());

        String chosenPlayer = gameOptions.chosenPlayer;
        logger.info("Starting with chosen player file: {}", chosenPlayer);

        // Register AlertBoxService
        Skin skin = new Skin(Gdx.files.internal("skins/rainbow/skin/rainbow-ui.json"),
                ServiceLocator.getResourceService().getAsset("skins/rainbow/skin/rainbow-ui.atlas", TextureAtlas.class));
        Stage stage = ServiceLocator.getRenderService().getStage();
        ServiceLocator.registerAlertBoxService(new AlertBoxService(stage, skin));

        // todo load based on what the user chose
        logger.info("Should start from save file: {}", shouldLoad);

        /*
         * based on the characters selected, changed the link
         * If Player choose Load, then create
         */
        this.playerFactory = new PlayerFactory(Arrays.stream(PLAYERS).toList());
        Entity player = loadPlayer(shouldLoad, gameOptions, chosenPlayer, gameOptions.difficulty);

        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising main game screen entities");

        MapLoadConfig mapConfig = new MapLoadConfig(); 
        mapConfig.currentLevel = "0";

        LevelFactory levelFactory = new MainGameLevelFactory(shouldLoad, mapConfig);

        if (gameOptions.difficulty == TEST) {
            new TestGameArea(gameArea, levelFactory, player);
        } else {
            new GameController(gameArea, levelFactory, player, shouldLoad, mapConfig);
        }
    }

    /**
     * Loads map data from the save file and extracts the completed rooms into a list which
     * is later set to completed in the create method above
     * @param filePath: Path for the save file
     */
    public MapLoadConfig loadFromJson(String filePath) {
        MapLoadConfig mapLoadConfig = new MapLoadConfig();
        mapLoadConfig  = FileLoader.readClass(MapLoadConfig.class, filePath, FileLoader.Location.EXTERNAL);
        return mapLoadConfig;
    }

    private Entity loadPlayer(boolean shouldLoad, GameOptions gameOptions, String chosenPlayer, Difficulty difficulty) {
        Entity player;
        if (shouldLoad) {
            PlayerConfig config = FileLoader.readClass(
                    PlayerConfig.class,
                    "configs/player_save.json",
                    FileLoader.Location.EXTERNAL);
            if (config == null) {
                throw new RuntimeException("Tried to load player and failed");
            }
            player = playerFactory.createPlayer(config, gameOptions.difficulty);
        } else {
            player = playerFactory.createPlayer(chosenPlayer, gameOptions.difficulty);
        }
        return player;
    }
}
