package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.services.ServiceLocator.getTimeSource;

/**
 * This class renders the final death image for the character.
 */
public class DeathPlayerAnimation extends UIComponent {
    private Texture playerDeath;

    /**
     * Time between completion of death animation and death screen, in milliseconds
     */
    private static final int DEATH_DELAY = 1000;

    public static final Logger logger = LoggerFactory.getLogger(DeathPlayerAnimation.class);
    private boolean didTriggerEvent;
    private boolean didSetTime;
    private long deathTime;

    @Override
    public void create() {
        super.create();
        // Initialize Texture
        playerDeath = new Texture("images/player/dead_corpse.png");
        didTriggerEvent = false;
        didSetTime = false;
    }

    /**
     * Draws the player corpse png after the player death animation has finished.
     * @param batch  the sprite batch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Ensure the player health is 0 and the animation has finished before drawing
        if (entity.getComponent(CombatStatsComponent.class).isDead()
                && entity.getComponent(PlayerAnimationController.class).stopAnimation()) {
                float textureWidth = playerDeath.getWidth();
                float textureHeight = playerDeath.getHeight();
                float aspectRatio = textureWidth / textureHeight;

                // Get the player position
                float x = entity.getPosition().x;
                float y = entity.getPosition().y;

                batch.draw(playerDeath, x, y,
                        0, 0, 1, 0.4f, // Size
                        1f, aspectRatio, 0f, 0, 0, playerDeath.getWidth(), playerDeath.getHeight(), // Source region
                        false, false);

            // Wait given amount of time before triggering event
            if (!didSetTime) {
                deathTime = getTimeSource().getTime();
                didSetTime = true;
            }
            if (!didTriggerEvent
                    && getTimeSource().getTimeSince(deathTime) > DEATH_DELAY) {
                entity.getEvents().trigger("player_finished_dying");
                logger.info("Player finished dying");
                didTriggerEvent = true;
            }
        }
    }

    /**
     * Dispose playerDeath resources to avoid memory leaks
     */
    @Override
    public void dispose() {
        super.dispose();
        this.playerDeath.dispose();
    }
}