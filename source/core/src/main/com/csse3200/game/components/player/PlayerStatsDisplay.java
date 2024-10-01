package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
    Table table;
    Table ammoTable;
    private Image heartImage;
    private Label healthLabel;
    private Label pickaxeLabel;
    private Label shotgunLabel;

    private Texture ammoTexture;
    private List<Image> ammoImages;
    private int currentAmmo;
    private static final int RELOAD_TIME = 3;
    float screenWidth = Gdx.graphics.getWidth();

    private Image speedImage;
    private Label speedLabelText;

    private ProgressBar speedProgressBar;

    private Image damageImage;
    private ProgressBar damageProgressBar;

    public static final String HEART_TEXTURE = "images/heart.png";
    public static final String SPEED_TEXTURE = "images/items/energy_drink.png";
    public static final String DAMAGE_BUFF_TEXTURE = "images/items/armor.png";



    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        ammoImages = new ArrayList<>();
        addActors();

        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("melee_pickup", this::updateMeleeWeaponUI);
        entity.getEvents().addListener("ranged_pickup", this::updateRangedWeaponUI);
        entity.getEvents().addListener("ranged_activate", this::updateAmmoDisplay);
        entity.getEvents().addListener("updateSpeedPercentage", this::updateSpeedPercentageUI);
        entity.getEvents().addListener("updateDamageBuff", this::updateDamageUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        ammoTable = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(45f).padLeft(5f);

        // Heart image
        float heartSideLength = 30f;
        heartImage = new Image(ServiceLocator.getResourceService().getAsset(HEART_TEXTURE, Texture.class));

        // Ammo image
        ammoTexture = new Texture(Gdx.files.internal("images/Weapons/ammo.png")); // Load ammo texture
        for (int i = 0; i < 20; i++) {
            Image ammoImage = new Image(ammoTexture);
            ammoImages.add(ammoImage);
        }

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Health: %d", health);
        healthLabel = new Label(healthText, skin, "small");

        //Speed image
        float speedSideLength = 100f;
        speedImage = new Image(ServiceLocator.getResourceService().getAsset(SPEED_TEXTURE, Texture.class));

        //Speed text
        speedProgressBar = new ProgressBar(0f, 1.5f, 0.1f, false, skin);
        speedProgressBar.setWidth(200f);
        speedProgressBar.setAnimateDuration(2.0f);
        /*
        //Temporarily commented out in case design team prefers text instead of progress bar
        float speedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        CharSequence speedText = String.format("Speed: %.1f%%", speedPercentage);
        speedLabelText = new Label(speedText, skin, "small");
         */

        //Damage Progress bar
        //Will need to check values
        float damageSideLength = 50f;
//        damageImage = new Image(
//                ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
        damageImage = new Image(ServiceLocator.getResourceService().getAsset(DAMAGE_BUFF_TEXTURE, Texture.class));
        damageProgressBar = new ProgressBar(0f, 5.0f, 0.1f, false, skin);
        damageProgressBar.setWidth(200f);
        damageProgressBar.setAnimateDuration(2.0f);

        //Weapon text, like the name of weapon
        //entity.getComponent(WeaponComponent.class).getWeaponType();
        pickaxeLabel = new Label("Pickaxe: 0", skin, "large");
        shotgunLabel = new Label("Shotgun: 0", skin, "large");


        table.add(heartImage).size(heartSideLength).pad(5);
        table.add(healthLabel).padLeft(10).left();

        table.row().padTop(10);
        table.add(speedImage).size(speedSideLength).pad(5);
        table.add(speedProgressBar).padLeft(10).left().width(200);

        //Don't know if values are correct, may overlap
        table.row().padTop(10);
        table.add(damageImage).size(damageSideLength).pad(5);
        table.add(damageProgressBar).padLeft(10).left().width(200);

        table.row().padTop(10);
        table.add(pickaxeLabel).colspan(2).padLeft(10).left();
        table.row().padTop(10);
        table.add(shotgunLabel).colspan(2).padLeft(10).left();
        table.row().padTop(10);
        table.add(ammoTable).colspan(2).left().padLeft(2);
        stage.addActor(table);
    }


    @Override
    public void draw(SpriteBatch batch) {

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

    public void updateMeleeWeaponUI() {
        CharSequence text = String.format("Pickaxe x %d", 1);
        pickaxeLabel.setText(text);
    }

    public void updateRangedWeaponUI(int maxAmmo) {
        CharSequence text = String.format("Shotgun x %d", 1);
        shotgunLabel.setText(text);

        currentAmmo = maxAmmo; // Initialize ammo count from WeaponComponent
        displayAllAmmo();
    }

    /**
     * Displays ammo based on the count of the shotgun or other ranged weapon.
     * This method is called when the shotgun count is greater than 0.
     * Displays ammo in rows of 8 within a separate ammo table.
     */
    private void displayAllAmmo() {
        ammoTable.clear(); // Clear existing ammo from the ammoTable
        ammoTable.top().left(); // Reset alignment
        int itemsInRow = 0;

        // Always display ammo up to currentAmmo
        for (int i = 0; i < currentAmmo; i++) {
            if (itemsInRow == 8) {
                ammoTable.row().padTop(2f); // Start a new row after 8 ammo items
                itemsInRow = 0;
            }
            ammoImages.get(i).setVisible(true);
            ammoTable.add(ammoImages.get(i)).size(screenWidth/45f);
            itemsInRow++;
        }

        ammoTable.setVisible(true); // Show ammo table when displaying ammo
    }

    /**
     * Updates the ammo display based on the current ammo count.
     * Ammo icons beyond the current count will be hidden.
     */
    private void updateAmmoDisplay(int ammoCount) {
        for (int i = 0; i < ammoImages.size(); i++) {
            if (i < ammoCount) {
                ammoImages.get(i).setVisible(true); // Show ammo that's still available
            } else {
                ammoImages.get(i).setVisible(false); // Hide depleted ammo
            }
        }
    }

    /**
     * Updates the player's speed on the ui.
     *
     * @param speedPercentage the player's new speed percentage to update the UI to
     */
    public void updateSpeedPercentageUI(float speedPercentage, String speedType) {
        //Temporarily commented out in case design team prefers text over a progress bar
//        CharSequence text = String.format("Speed: %.1f%%", speedPercentage);
//        speedLabelText.setText(text);
        speedProgressBar.setValue(speedPercentage);
    }

    public void updateDamageUI(int damage) {
        damageProgressBar.setValue(damage);
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
    }
}
