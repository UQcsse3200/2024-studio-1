package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
    Table table;
    private Image heartImage;
    private Label healthLabel;
    private Label pickaxeLabel;
    private Label shotgunLabel;

    private Image speedImage;
    private Label speedLabel;

    private ProgressBar speedLabel2;



    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
//        shapeRenderer = new ShapeRenderer();
        addActors();

        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("updateMeleeWeaponCount", this::updateMeleeWeaponCountUI);
        entity.getEvents().addListener("updateRangedWeaponCount", this::updateRangedWeaponCountUI);
        entity.getEvents().addListener("updateSpeedPercentage", this::updateSpeedPercentageUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(45f).padLeft(5f);

        // Heart image
        float heartSideLength = 30f;
        heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Health: %d", health);
        healthLabel = new Label(healthText, skin, "small");

        //Speed image
        float speedSideLength = 30f;
        speedImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

        //Speed text
        float speedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        CharSequence speedText = String.format("Speed: %.1f%%", speedPercentage);
        speedLabel = new Label(speedText, skin, "small");
        speedLabel2 = new ProgressBar(0f, 5.0f, 0.1f, false, skin);
        speedLabel2.setWidth(200f);
        speedLabel2.setAnimateDuration(2.0f);



        //Weapon text, like the name of weapon
        //entity.getComponent(WeaponComponent.class).getWeaponType();
        pickaxeLabel = new Label("Pickaxe: 0", skin, "large");
        shotgunLabel = new Label("Shotgun: 0", skin, "large");


        table.add(heartImage).size(heartSideLength).pad(5);
        table.add(healthLabel).padLeft(10).left();

        table.row().padTop(10);
        table.add(speedImage).size(speedSideLength).pad(5);
        table.add(speedLabel2).padLeft(10).left().width(200);

        table.row().padTop(10);
        table.add(pickaxeLabel).colspan(2).padLeft(10).left();
        table.row().padTop(10);
        table.add(shotgunLabel).colspan(2).padLeft(10).left();
        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch) {
//        batch.end();
//
//        // Set up the projection matrix for rendering
//        Matrix4 projectionMatrix = batch.getProjectionMatrix().cpy();
//        shapeRenderer.setProjectionMatrix(projectionMatrix);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        // Calculate health percentage and position
//        float speedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
//        float totalSpeedPercentage = (float) speedPercentage / 5.0f;
//
//        float x = 0f;//entity.getPosition().x;
//        float y = entity.getPosition().y + 3;
//
//        // Draw background (red)
//        shapeRenderer.setColor(Color.PURPLE);
//        shapeRenderer.rect(x, y, 1f, 0.1f);
//
//        // Draw health (green)
//        shapeRenderer.setColor(Color.ORANGE);
//        shapeRenderer.rect(x, y, 1f * totalSpeedPercentage, 0.1f);
//
//        shapeRenderer.end();
//
//        batch.begin();

        // draw is handled by the stage
    }

    /**
     * Updates the player's health on the ui.
     *
     * @param health player health
     */
    public void updatePlayerHealthUI(int health) {
        CharSequence text = String.format("Health: %d", health);
        healthLabel.setText(text);
    }

    public void updateMeleeWeaponCountUI(int weaponCount) {
        CharSequence text = String.format("Pickaxe x %d", weaponCount);
        pickaxeLabel.setText(text);
    }

    public void updateRangedWeaponCountUI(int weaponCount) {
        CharSequence text = String.format("Shotgun x %d", weaponCount);
        shotgunLabel.setText(text);
    }

    public void updateSpeedPercentageUI(float speedPercentage) {
        CharSequence text = String.format("Speed: %.1f%%", speedPercentage);
        speedLabel.setText(text);
        speedLabel2.setValue(speedPercentage);
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
    }
}
