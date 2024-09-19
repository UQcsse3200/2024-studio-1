package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.ui.UIComponent;

/**
 * Display's Player's health on UI as a health bar above player's head
 */
public class PlayerHealthDisplay extends UIComponent{
    /** Dimensions of health bar */
    private static final float WIDTH = 1.5f;
    private static final float HEIGHT = 0.1f;
    /** Constant used to calculate the position of health bar on UI*/
    private final static float X_BAR = 0.3f;
    private final static float Y_BAR = 1.2f;
    /** Shape rendered for drawing the health bar */
    private ShapeRenderer shapeRenderer;
    private boolean sheildActivated = false;



    public PlayerHealthDisplay() {

    }
    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        if (entity.getEvents() != null) {
            entity.getEvents().addListener("shieldActivated",this::activateShield);
            entity.getEvents().addListener("shieldDeactivated", this::deactivateShield);
        }
        shapeRenderer = new ShapeRenderer();

    }

    public void updateHealth() {
        if (sheildActivated) {
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(entity.getPosition().x - X_BAR,
                    entity.getPosition().y + Y_BAR + HEIGHT, WIDTH, HEIGHT);
        }
    }

    public void activateShield() {
        sheildActivated = true;
    }
    public void deactivateShield() {
        sheildActivated = false;
        updateHealth();
    }

    /**
     * Draws the health bar over player's head
     * The bar is red in colour and the green filling over the red bar
     * @param batch  the sprite batch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch)  {
        // end the current batch to prepare for custom shape renderer drawing
        batch.end();
        // copy the current projection matrix for use
        Matrix4 projectionMatrix = batch.getProjectionMatrix().cpy();
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // get player's current health percentage
        CombatStatsComponent stats = entity.getComponent(CombatStatsComponent.class);
        float healthPercent = (float) stats.getHealth() / 100;

        // Draw full health bar in red with defined position and size
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(entity.getPosition().x - X_BAR,
                entity.getPosition().y + Y_BAR, WIDTH, HEIGHT);

        // Draw green bar on top of red bar with the width of health percentage
        float greenFill = WIDTH * healthPercent;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(entity.getPosition().x - X_BAR,
                entity.getPosition().y + Y_BAR, greenFill, HEIGHT );

        updateHealth();
        shapeRenderer.end();
        batch.begin();

    }

    /**
     * Dispose the resources used by shape rendered that are no longer needed
     */
    public void dispose() {
        super.dispose();
//        shapeRenderer.dispose();
    }

}
