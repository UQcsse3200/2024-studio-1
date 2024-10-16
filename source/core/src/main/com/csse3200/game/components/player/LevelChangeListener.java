package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles level change
 */
public class LevelChangeListener extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LevelChangeListener.class);
    private boolean didTriggerEvent;
    private int currentLevel;

    @Override
    public void create() {
        super.create();
        didTriggerEvent = false;
        currentLevel = 0;
    }

    /**
     * Draws the player corpse png after the player death animation has finished.
     * @param batch  the sprite batch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        logger.debug("Drawing the corpse");
        int tempLevel = ServiceLocator.getGameAreaService().getGameController().getCurrentLevelNumber();

        if (tempLevel > currentLevel) {
            ServiceLocator.getGameAreaService().getGameController().updateLevel();
            currentLevel = tempLevel;
        }

        if (currentLevel >= 3 && !didTriggerEvent) {
            logger.info("Level won: " + entity.getId());
            entity.getEvents().trigger("player_win");
            didTriggerEvent = true;
        }
    }

    /**
     * Dispose playerDeath resources to avoid memory leaks
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}