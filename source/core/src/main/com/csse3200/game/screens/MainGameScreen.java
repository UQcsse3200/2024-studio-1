package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlayerSelection;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;
import static com.csse3200.game.GdxGame.ScreenType.LOSE;
import static com.csse3200.game.entities.PlayerSelection.PLAYERS;
import static com.csse3200.game.options.GameOptions.Difficulty.TEST;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class MainGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
    private final PlayerFactory playerFactory;
    private static final String[] mainGameTextures = {
            "images/heart.png", "images/ui_white_icons.png", "images/ui_white_icons_over.png",
            "images/ui_white_icons_down.png","skins/rainbow/skin/rainbow-ui.png", "images/dot_transparent.png", "images/black_dot_transparent.png"
    };
    // todo may not be needed
    private static final String[] mainGameAtlases = {"flat-earth/skin/flat-earth-ui.atlas"};

    private PlayerSelection playerSelection = new PlayerSelection();

    private static final String[] textureAtlases = {"skins/rainbow/skin/rainbow-ui.atlas"};

    /**
     * Array of font file paths used in the game.
     * These fonts are loaded as assets and can be used for various UI elements.
     */

    private static final String[] fonts = {
            "skins/rainbow/skin/font-button-export.fnt", "skins/rainbow/skin/font-export.fnt",
            "skins/rainbow/skin/font-title-export.fnt"
    };


    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 5.5f);
    private static final Vector2 MINIMAP_CAMERA_POSITION = new Vector2(-7.5f, 4.8f);

    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private Entity ui;
    public static boolean isPaused = false;

    public MainGameScreen(GdxGame game) {
        this.game = game;
        game.setScreenColour(GdxGame.ScreenColour.GREY);
        isPaused = false;

        GameOptions gameOptions = game.gameOptions;
        logger.info("Starting game with difficulty {}", gameOptions.difficulty.toString());

        logger.debug("Initialising main game screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerRandomService(new RandomService("Default Seed :p"));

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        this.physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        this.renderer = RenderFactory.createRenderer();
        this.renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        this.renderer.getSecondaryCamera().getEntity().setPosition(MINIMAP_CAMERA_POSITION);
        this.renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        ServiceLocator.getRenderService().setCamera(this.renderer.getCamera());
        ServiceLocator.getRenderService().setSecondaryCamera(this.renderer.getSecondaryCamera());

        loadAssets();
        createUI();

        String chosenPlayer = gameOptions.chosenPlayer;
        logger.info("Starting with chosen player file: {}", chosenPlayer);

        // Register AlertBoxService
        Skin skin = new Skin(Gdx.files.internal("skins/rainbow/skin/rainbow-ui.json"),
                ServiceLocator.getResourceService().getAsset("skins/rainbow/skin/rainbow-ui.atlas", TextureAtlas.class));
        Stage stage = ServiceLocator.getRenderService().getStage();
        ServiceLocator.registerAlertBoxService(new AlertBoxService(stage, skin));

        // todo load based on what the user chose
        boolean shouldLoad = gameOptions.shouldLoad;
        logger.info("Should start from save file: {}", shouldLoad);

        /*
         * based on the characters selected, changed the link
         * If Player choose Load, then create
         */
        this.playerFactory = new PlayerFactory(Arrays.stream(PLAYERS).toList());
        Entity player = loadPlayer(shouldLoad, gameOptions, chosenPlayer);

        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising main game screen entities");

        LevelFactory levelFactory = new MainGameLevelFactory(shouldLoad);

        if (gameOptions.difficulty == TEST) {
            new TestGameArea(levelFactory, player);
        } else {
            new MainGameArea(levelFactory, player, shouldLoad);
        }
    }

    private Entity loadPlayer(boolean shouldLoad, GameOptions gameOptions, String chosenPlayer) {
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

    @Override
    public void render(float delta) {

        // The ui should be updated, whether paused or unpaused. Other entities may not need to
        // be updated yet
        ui.earlyUpdate();
        ui.update();

        if (isPaused) {
            // Render just the ui
            renderer.render();
            // Exit early, don't update other entities
            return;
        }

        // Not paused, so update everything
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        ServiceLocator.getGameAreaService().update();

        // Re-render for the non-ui entities
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        ui.getComponent(MainGameExitDisplay.class).resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Game paused, {}", ServiceLocator.getEntityService());
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing main game screen");

        renderer.dispose();
        playerFactory.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainGameTextures);
        resourceService.loadTextureAtlases(mainGameAtlases);
        resourceService.loadTextureAtlases(textureAtlases);
        resourceService.loadFonts(fonts);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
    }

    private void loseGame() {
        logger.info("Received event: player finished dying");
        game.setScreen(LOSE);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        ui = new Entity();
        ui.addComponent(new NameComponent("Main Game Screen UI"))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal(this.game))
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        // When player finishes dying, go to death screen
        logger.info("Add listener for player finished dying");
        ui.getEvents().addListener("player_finished_dying", this::loseGame);

        ServiceLocator.getEntityService().register(ui);
    }
}
