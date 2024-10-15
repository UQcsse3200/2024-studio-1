package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.csse3200.game.GdxGame.ScreenType.LOSE;
import static com.csse3200.game.GdxGame.ScreenType.WIN;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class GameScreen extends ScreenAdapter {
    public static final Logger logger = LoggerFactory.getLogger(GameScreen.class);
    public static final String[] mainGameTextures = {
            "images/heart.png",
            "images/ui_white_icons.png",
            "images/ui_white_icons_over.png",
            "images/ui_white_icons_down.png",
            "skins/rainbow/skin/rainbow-ui.png",
            "images/dot_transparent.png",
            "images/black_dot_transparent.png"
    };
    // todo may not be needed
    public static final String[] mainGameAtlases = {"flat-earth/skin/flat-earth-ui.atlas"};

    public static final String[] textureAtlases = {"skins/rainbow/skin/rainbow-ui.atlas"};

    /**
     * Array of font file paths used in the game.
     * These fonts are loaded as assets and can be used for various UI elements.
     */

    public static final String[] fonts = {
            "skins/rainbow/skin/font-button-export.fnt", "skins/rainbow/skin/font-export.fnt",
            "skins/rainbow/skin/font-title-export.fnt"
    };


    public static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 5.5f);
    public static final Vector2 MINIMAP_CAMERA_POSITION = new Vector2(-7.5f, 4.8f);

    public final GdxGame game;
    public final Renderer renderer;
    public final PhysicsEngine physicsEngine;
    public Entity ui;
    public static boolean isPaused = false;

    public GameScreen(GdxGame game) {
        this.game = game;
        game.setScreenColour(GdxGame.ScreenColour.GREY);
        isPaused = false;

        logger.debug("Initialising game screen services");
        ServiceLocator.registerTimeSource(new GameTime());

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

        // Register AlertBoxService
        Skin skin = new Skin(Gdx.files.internal("skins/rainbow/skin/rainbow-ui.json"),
                ServiceLocator.getResourceService().getAsset("skins/rainbow/skin/rainbow-ui.atlas", TextureAtlas.class));
        Stage stage = ServiceLocator.getRenderService().getStage();
        ServiceLocator.registerAlertBoxService(new AlertBoxService(stage, skin));

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
        logger.debug("Disposing game screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    public void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainGameTextures);
        resourceService.loadTextureAtlases(mainGameAtlases);
        resourceService.loadTextureAtlases(textureAtlases);
        resourceService.loadFonts(fonts);
        ServiceLocator.getResourceService().loadAll();
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
    }

    public void loseGame() {
        logger.info("Received event: player finished dying");
        game.setScreen(LOSE);
    }

    public void winGame() {
        logger.info("Received event: player won!");
        game.setScreen(WIN);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    public void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        ui = new Entity();
        ui.addComponent(new NameComponent("Game Screen UI"))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal(this.game))
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        // When player finishes dying, go to death screen
        logger.info("Add listener for player finished dying and winnning");

        ui.getEvents().addListener("player_win", this::winGame);

        ui.getEvents().addListener("player_finished_dying", this::loseGame);

        ServiceLocator.getEntityService().register(ui);
    }
}