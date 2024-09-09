package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class renders the final death image for the character.
 */
public class DeathPlayerAnimation extends UIComponent {
    private Texture playerDeath;

    public static final Logger logger = LoggerFactory.getLogger(DeathPlayerAnimation.class);

    @Override
    public void create() {
        super.create();
        // Initialize Texture
        playerDeath = new Texture("images/player/dead_corpse.png");
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