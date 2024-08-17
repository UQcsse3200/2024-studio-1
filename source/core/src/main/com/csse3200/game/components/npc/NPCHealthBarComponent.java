package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class NPCHealthBarComponent extends RenderComponent {
    private static final float WIDTH = 1f;
    private static final float HEIGHT = 0.1f;
    private static final float OFFSET_Y = 1.2f;

    private CombatStatsComponent combatStats;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.end();

        Matrix4 projectionMatrix = batch.getProjectionMatrix().cpy();
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float healthPercentage = (float) combatStats.getHealth() / combatStats.getMaxHealth();
        float x = entity.getPosition().x;
        float y = entity.getPosition().y + OFFSET_Y;

        // Draw background
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, WIDTH, HEIGHT);

        // Draw health
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, WIDTH * healthPercentage, HEIGHT);

        shapeRenderer.end();

        batch.begin();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}