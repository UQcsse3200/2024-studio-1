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

    /**
     * Create a new static screen.
     *
     * @param game     the game that this screen is associated with.
     * @param textures array of textures to load.
     * @param logger   the logger for this class.
     */
    public StaticScreen(GdxGame game, String[] textures, Logger logger) {
        this.game = game;
        this.textures = textures;
        this.logger = logger;

        logger.debug("Initialising services for {}", this.getClass().getSimpleName());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
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
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(textures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(textures);
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
        Entity ui = getUI();
        ServiceLocator.getEntityService().register(ui);
    }

}
