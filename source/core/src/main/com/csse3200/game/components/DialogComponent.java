package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.files.UserSettings;

/**
 * This component renders a health bar for NPCs.
 * It displays the current health as a percentage of maximum health above the NPC.
 */
public class DialogComponent extends RenderComponent {
    public static final float PADDING = 0.1f;
    public static float WIDTH = 0f;
    public static final float HEIGHT = 0.4f+PADDING*2;
    public static final float OFFSET_Y = 1f;

    private static String text = "";
    private static String glyphText = "";
    private static float textLength = 0f;
    private static float fps = 60f;
    private static float framesPerChar = 5f;

    private CombatStatsComponent combatStats;
    private NameComponent nameComponent;
    ShapeRenderer shapeRenderer;

    private GlyphLayout layout;

    /**
     * Called when the entity is created and registered. Initializes the ShapeRenderer.
     */
    @Override
    public void create() {
        super.create();
        // Get the CombatStatsComponent and initialize the ShapeRenderer
        combatStats = entity.getComponent(CombatStatsComponent.class);
        nameComponent = entity.getComponent(NameComponent.class);
        shapeRenderer = new ShapeRenderer();

        UserSettings.Settings settings = UserSettings.get();
        fps = settings.fps;

        fnt_18.setColor(Color.BLACK);

        layout = new GlyphLayout();
        //showDialog("remove this sample dialog from DialogComponent");
        //completeDialog();
        //dismissDialog();
    }

    public void showDialog(String text) {
        this.text = text;
        glyphText = "";
        textLength = 0f;
    }

    private void completeDialog() {
        glyphText = text;
        layout.setText(fnt_18, glyphText);
        textLength = text.length();
        WIDTH = layout.width*projectionFactor+PADDING*2;
    }

    //returns true if dialog is dismissed
    public boolean dismissDialog() {
        if(glyphText.length() == text.length())
        {
            this.text = "";
            return true;
        }
        else
        {
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
        if(!text.equals(""))
        {
            if(glyphText.length() < text.length())
            {
                textLength += 1/framesPerChar;
                if((int)textLength > glyphText.length())
                {
                    glyphText = text.substring(0, (int)textLength);
                    layout.setText(fnt_18, glyphText);
                    WIDTH = layout.width*projectionFactor+PADDING*2;
                }
            }
            batch.end();

            // Set up the projection matrix for rendering
            Matrix4 projectionMatrix = batch.getProjectionMatrix().cpy();
            shapeRenderer.setProjectionMatrix(projectionMatrix);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Calculate health percentage and position
            float healthPercentage = (float) combatStats.getHealth() / combatStats.getMaxHealth();
            float x = entity.getPosition().x;
            float y = entity.getPosition().y + OFFSET_Y;

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x, y, WIDTH, HEIGHT);

            shapeRenderer.end();

            batch.begin();
            batch.setProjectionMatrix(projectionMatrix.cpy().scale(projectionFactor, projectionFactor, 1));
            fnt_18.draw(batch, glyphText, (x+PADDING)/projectionFactor,(y+PADDING)/projectionFactor+fnt_18.getCapHeight()*1.5f);
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