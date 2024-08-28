package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class BlackoutComponent extends RenderComponent {

    public final ShapeRenderer shapeRenderer;
    private final Stage stage;
    private float alpha = 0f;
    private boolean fadingIn = false;
    private boolean fadingOut = false;
    private boolean blackoutActive = false;

    public BlackoutComponent() {
        this.shapeRenderer = new ShapeRenderer();
        this.stage = ServiceLocator.getRenderService().getStage();
    }

    public void triggerBlackout(float duration) {
        fadingIn = true;
        fadingOut = false;
        alpha = 0f;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadingIn = false;
                fadingOut = true;
                blackoutActive = true;
            }
        }, duration / 2f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadingOut = false;
                blackoutActive = false;
            }
        }, duration);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!fadingIn && !fadingOut && !blackoutActive) {
            return;
        }

        // End the batch before using ShapeRenderer
        batch.end();

        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, alpha));
        shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
        shapeRenderer.end();

        // Restart the batch for subsequent SpriteBatch rendering
        batch.begin();

        if (fadingIn) {
            alpha += 0.02f;
            if (alpha >= 1f) {
                alpha = 1f;
                fadingIn = false;
            }
        } else if (fadingOut) {
            alpha -= 0.02f;
            if (alpha <= 0f) {
                alpha = 0f;
                fadingOut = false;
                blackoutActive = false;
            }
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isFadingIn() {
        return fadingIn;
    }

    public boolean isFadingOut() {
        return fadingOut;
    }

    public boolean isBlackoutActive() {
        return blackoutActive;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        super.dispose();
    }
}