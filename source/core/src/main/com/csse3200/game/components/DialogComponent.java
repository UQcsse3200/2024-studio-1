package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.files.UserSettings;

/**
 * This component renders a health bar for NPCs.
 * It displays the current health as a percentage of maximum health above the NPC.
 */
public class DialogComponent extends RenderComponent {
    public static final float PADDING = 0.1f;
    private static float width = 0f;
    public static final float HEIGHT = 0.4f + PADDING * 2;
    public static final float OFFSET_Y = 1.5f;

    private static String text = "";
    private static String glyphText = "";
    private static float textLength = 0f;
    private static final float FRAMES_PER_CHAR = 5f;

    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;

    /**
     * Called when the entity is created and registered. Initializes the ShapeRenderer.
     */
    @Override
    public void create() {
        super.create();
        entity.getComponent(CombatStatsComponent.class);
        shapeRenderer = new ShapeRenderer();

        UserSettings.get();

        fnt_18.setColor(Color.BLACK);

        layout = new GlyphLayout();
    }

    public void showDialog(String newText) {
        text = newText;
        glyphText = "";
        textLength = 0f;
    }

    private void completeDialog() {
        glyphText = text;
        layout.setText(fnt_18, glyphText);
        textLength = text.length();
        width = layout.width * projectionFactor + PADDING * 2;
    }

    //returns true if dialog is dismissed
    public boolean dismissDialog() {
        if (glyphText.length() == text.length()) {
            text = "";
            return true;
        } else {
            completeDialog();
            return false;
        }
    }

    /**
     * Draws the NPC's health bar above the entity.
     * The health bar reflects the current health as a percentage of the maximum health.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (!text.isEmpty()) {
            if (glyphText.length() < text.length()) {
                textLength += 1 / FRAMES_PER_CHAR;
                if ((int) textLength > glyphText.length()) {
                    glyphText = text.substring(0, (int) textLength);
                    layout.setText(fnt_18, glyphText);
                    width = layout.width * projectionFactor + PADDING * 2;
                }
            }
            batch.end();

            // Set up the projection matrix for rendering
            Matrix4 projectionMatrix = batch.getProjectionMatrix().cpy();
            shapeRenderer.setProjectionMatrix(projectionMatrix);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Calculate position
            float x = entity.getPosition().x;
            float y = entity.getPosition().y + OFFSET_Y;

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x, y, width, HEIGHT);

            shapeRenderer.end();

            batch.begin();
            batch.setProjectionMatrix(projectionMatrix.cpy().scale(projectionFactor, projectionFactor, 1));
            fnt_18.draw(batch, glyphText, (x + PADDING) / projectionFactor, (y + PADDING) / projectionFactor + fnt_18.getCapHeight() * 1.5f);
            batch.setProjectionMatrix(projectionMatrix);
        }
    }

    /**
     * Disposes of the resources used by this component, specifically the ShapeRenderer.
     */
    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}