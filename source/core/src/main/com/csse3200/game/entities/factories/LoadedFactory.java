package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that uses a default strategy for loading assets from the resource service.
 */
public abstract class LoadedFactory implements Disposable {
    private final Logger logger;

    /**
     * Create a loaded factory that uses default logging.
     */
    protected LoadedFactory() {
        this(LoggerFactory.getLogger(LoadedFactory.class));
    }

    /**
     * Create a loaded factory that uses a provided logger.
     *
     * @param logger the logger this factory should send log messages to.
     */
    protected LoadedFactory(Logger logger) {
        this.logger = logger;
        load();
    }

    /**
     * Get all the filepaths to sounds needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getSoundFilepaths() {
        return new String[]{};
    }

    /**
     * Get all the filepaths to texture atlases needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getTextureAtlasFilepaths() {
        return new String[]{};
    }

    /**
     * Get all the filepaths to textures needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getTextureFilepaths() {
        return new String[]{};
    }

    /**
     * Get all the filepaths to music needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getMusicFilepaths() {
        return new String[]{};
    }

    private void logLoaded(String type, String[] paths) {
        if (paths.length == 0) {
            return;
        }
        logger.info("Loaded {} Files:\n{}", type, String.join("\n", paths));
    }

    /**
     * load all the assets needed by this factory.
     */
    public void load() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(getTextureFilepaths());
        resourceService.loadTextureAtlases(getTextureAtlasFilepaths());
        resourceService.loadSounds(getSoundFilepaths());
        resourceService.loadMusic(getMusicFilepaths());

        while (!resourceService.loadForMillis(20)) {
            logger.info("Loading... {}%", resourceService.getProgress());
        }
        logLoaded("Texture", getTextureFilepaths());
        logLoaded("Atlas", getTextureAtlasFilepaths());
        logLoaded("Sound", getSoundFilepaths());
        logLoaded("Music", getMusicFilepaths());
    }

    @Override
    public void dispose() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(getTextureFilepaths());
        resourceService.unloadAssets(getTextureAtlasFilepaths());
        resourceService.unloadAssets(getSoundFilepaths());
        resourceService.unloadAssets(getMusicFilepaths());

        logger.info("Unloaded Texture Files:\n{}", String.join("\n", getTextureFilepaths()));
        logger.info("Unloaded Atlas Files:\n{}", String.join("\n", getTextureAtlasFilepaths()));
        logger.info("Unloaded Sound Files:\n{}", String.join("\n", getSoundFilepaths()));
        logger.info("Unloaded Music Files:\n{}", String.join("\n", getMusicFilepaths()));
    }
}
