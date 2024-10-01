package com.csse3200.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Stat bar on player select screen to show player stats e.g. health, attack, probably more we'll
 * see.
 * Adapted from <a href="https://stackoverflow.com/a/34668612">Stack Overflow</a>.
 */
public class StatBar extends Actor {

    private static final Logger logger = getLogger(StatBar.class);
    private Texture texture;
    private final float proportion;
    /**
     * Whether the texture needs to be recreated e.g. after screen resizing.
     */
    private boolean shouldUpdateTexture;

    public StatBar(float proportion) {
        this.proportion = proportion;
        shouldUpdateTexture = true; // create texture initially
    }

    /**
     * Create the texture using a pixmap. Could use ShapeRenderer instead.
     */
    private void createTexture() {

        // Get size of actor
        int width = (int) getWidth();
        int height = (int) getHeight();

        // Make pixmap
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillRectangle(
                0, 0, (int) (width * proportion), height);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(
                (int) (width * proportion), 0, (int) (width * (1 - proportion)), height);

        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    protected void positionChanged() {
        shouldUpdateTexture = true;
        super.positionChanged();
    }

    @Override
    protected void sizeChanged() {
        shouldUpdateTexture = true;
        super.sizeChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Only remake the texture when needed
        if (shouldUpdateTexture) {
            logger.info("Making texture with width {}, height {}", getWidth(), getHeight());
            createTexture();
            shouldUpdateTexture = false;
        }

        batch.draw(texture, getX(), getY());
    }

}