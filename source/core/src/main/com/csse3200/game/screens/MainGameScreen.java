package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenColour;
import com.csse3200.game.areas.*;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
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

import java.util.List;

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
    private static final String[] mainGameTextures = {
            "images/heart.png", "images/ui_white_icons.png", "images/ui_white_icons_over.png",
            "images/ui_white_icons_down.png", "flat-earth/skin/flat-earth-ui.png",
            "images/black_dot_transparent.png"
    };
    private static final String[] mainGameAtlases = {"flat-earth/skin/flat-earth-ui.atlas"};
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
    private final PlayerFactory playerFactory;
    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private final PlayerSelection playerSelection = new PlayerSelection();
    private Entity ui;
    public static boolean isPaused=false;

    public MainGameScreen(GdxGame game) {
        this.game = game;
        game.setScreenColour(ScreenColour.DEFAULT);

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

        ServiceLocator.registerCollectibleFactoryService(new CollectibleFactoryService());

        this.renderer = RenderFactory.createRenderer();
        this.renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        this.renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        ServiceLocator.getRenderService().setCamera(this.renderer.getCamera());

        loadAssets();
        createUI();

        String chosenPlayer = gameOptions.chosenPlayer;
        logger.info("Starting with chosen player file: {}", chosenPlayer);

        /*
         * based on the characters selected, changed the link
         * If Player choose Load, then create
         */
        this.playerFactory = new PlayerFactory(List.of(PLAYERS));
        Entity player = playerFactory.createPlayer(
                FileLoader.readClass(PlayerConfig.class, chosenPlayer));
        // todo refactor events to be more decoupled
        player.getEvents().addListener("player_finished_dying", this::loseGame);

        // todo ask character team, is this needed?
        // List<Entity> players = playerSelection.createTwoPlayers();

        logger.debug("Initialising main game screen entities");
        LevelFactory levelFactory = new MainGameLevelFactory();
        GameArea mainGameArea = (gameOptions.difficulty == TEST) ?
                new TestGameArea(levelFactory) :
                new MainGameArea(levelFactory);
        mainGameArea.create(player);
    }

    @Override
    public void render(float delta) {
        renderer.render();
        if ( isPaused) {
            return;
        }

        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        ServiceLocator.getGameAreaService().update();

    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        ui.getComponent(MainGameExitDisplay.class).resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Game paused");
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
        ui.addComponent(new InputDecorator(stage, 10))
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
