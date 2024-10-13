package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;

/**
 * A superclass for static screens such as menu screens. Aims to reduce boilerplate screen code.
 */
public abstract class StaticScreen extends ScreenAdapter {

    protected final Logger logger;
    protected final String[] textures;
    protected final GdxGame game;
    private final Renderer renderer;
    private final String music;
    protected Entity ui;

    /**
     * Create a new static screen with music.
     *
     * @param game     the game that this screen is associated with.
     * @param textures array of textures to load.
     * @param logger   the logger for this class.
     * @param bgColour screen background colour.
     * @param music    music path.
     * @param loop     whether to loop the music.
     */
    public StaticScreen(GdxGame game, String[] textures, Logger logger,
                        GdxGame.ScreenColour bgColour, String music, boolean loop) {
        this.game = game;
        this.textures = textures;
        this.logger = logger;
        this.music = music;

        game.setScreenColour(bgColour);

        logger.debug("Initialising services for {}", this.getClass().getSimpleName());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
        loadAndStartMusic(loop);
    }

    /**
     * Create a new static screen with no music.
     *
     * @param game     the game that this screen is associated with.
     * @param textures array of textures to load.
     * @param logger   the logger for this class.
     * @param bgColour screen background colour.
     */
    public StaticScreen(GdxGame game, String[] textures, Logger logger,
                        GdxGame.ScreenColour bgColour) {
        this(game, textures, logger, bgColour, null, false);
    }

    private void loadAndStartMusic(boolean loop) {
        if (music != null) {
            ResourceService resourceService = ServiceLocator.getResourceService();
            resourceService.loadMusic(new String[]{music});
            resourceService.loadAll();
            resourceService.playMusic(music, loop);
        }
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
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
        logger.debug("Disposing screen for {}", this.getClass().getSimpleName());

        renderer.dispose();
        unloadAssets();
        if (music != null) {
            ServiceLocator.getResourceService().stopCurrentMusic();
        }
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(textures);
        resourceService.loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(textures);
        if (music != null) {
            resourceService.unloadAssets(new String[]{music});
        }
    }

    /**
     * Create the UI for this screen including components for rendering UI elements to the screen
     * and capturing and handling ui input.
     *
     * @return this screen's UI with components added.
     */
    protected abstract Entity getUI();

    /**
     * Add the UI to the entity service.
     */
    private void createUI() {
        logger.debug("Creating ui");
        ui = getUI();
        ServiceLocator.getEntityService().register(ui);
    }

}
