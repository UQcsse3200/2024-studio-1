package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoadedFactory implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(LoadedFactory.class);

    protected LoadedFactory() {
        this(logger);
    }

    protected LoadedFactory(Logger logger) {
        load(logger);
    }

    /**
     * Get all the filepaths to sounds needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getSoundFilepaths(){
        return new String[]{};
    }

    /**
     * Get all the filepaths to texture atlases needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getTextureAtlasFilepaths(){
        return new String[]{};
    }

    /**
     * Get all the filepaths to textures needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getTextureFilepaths(){
        return new String[]{};
    }

    /**
     * Get all the filepaths to music needed by this Factory
     *
     * @return the filepaths needed.
     */
    protected String[] getMusicFilepaths(){
        return new String[]{};
    }


    private void logLoaded(String type, String[] paths){
        if (paths.length == 0){
            return;
        }
        logger.info("Loaded Texture Files:\n{}", String.join("\n", paths));
    }
    /**
     * load all the assets needed by this factory.
     *
     * @param logger which logger to report to.
     */
    public void load(Logger logger) {
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

    /**
     * load all the assets needed by this factory.
     */
    public void load() {
        load(logger);
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
