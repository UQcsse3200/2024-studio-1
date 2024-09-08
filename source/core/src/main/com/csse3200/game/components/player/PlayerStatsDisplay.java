package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A UI component for displaying player stats, e.g. health.
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
    private WeaponComponent weaponComponent;
    private static final int RELOAD_TIME = 3;

    /**
     * Creates reusable UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        ammoImages = new ArrayList<>();
        addActors();

        weaponComponent = entity.getComponent(WeaponComponent.class);

        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("updateMeleeWeaponCount", this::updateMeleeWeaponCountUI);
        entity.getEvents().addListener("updateRangedWeaponCount", this::updateRangedWeaponCountUI);
        entity.getEvents().addListener("RANGED_ATTACK", this::onPlayerAttack);
        entity.getEvents().addListener("RELOAD", this::startReload);
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
        heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

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

        // Weapon text
        pickaxeLabel = new Label("Pickaxe x 0", skin, "large");
        shotgunLabel = new Label("Shotgun x 0", skin, "large");

        table.add(heartImage).size(heartSideLength).pad(5);
        table.add(healthLabel).padLeft(10).left();
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
        // Draw is handled by the stage
    }

    /**
     * Updates the player's health on the UI.
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

        if (weaponCount > 0) {
            currentAmmo = weaponComponent.getAmmo(); // Initialize ammo count from WeaponComponent
            displayAllAmmo();
        } else {
            hideAmmo();
        }
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
        for (int i = 0; i < 20; i++) {
            if (itemsInRow == 8) {
                ammoTable.row().padTop(2f); // Start a new row after 8 ammo items
                itemsInRow = 0;
            }
            if (i < currentAmmo) {
                ammoImages.get(i).setVisible(true);
            } else {
                ammoImages.get(i).setVisible(false);
            }
            ammoTable.add(ammoImages.get(i)).size(30f);
            itemsInRow++;
        }

        ammoTable.setVisible(true); // Show ammo table when displaying ammo
    }

    /**
     * Hides all the ammo icons by clearing the ammoTable.
     */
    private void hideAmmo() {
        ammoTable.clear(); // Clear all items from the ammo table
        ammoTable.setVisible(false); // Hide the ammo table
    }

    /**
     * Handles the player's attack action by reducing the ammo count.
     */
    private void onPlayerAttack() {
        if (currentAmmo > 0) {
            currentAmmo--; // Decrease ammo count
            weaponComponent.setAmmo(currentAmmo); // Update ammo in WeaponComponent
            updateAmmoDisplay(); // Update the UI accordingly
        }
    }

    /**
     * Updates the ammo display based on the current ammo count.
     * Ammo icons beyond the current count will be hidden.
     */
    private void updateAmmoDisplay() {
        for (int i = 0; i < ammoImages.size(); i++) {
            if (i < currentAmmo) {
                ammoImages.get(i).setVisible(true); // Show ammo that's still available
            } else {
                ammoImages.get(i).setVisible(false); // Hide depleted ammo
            }
        }
    }

    /**
     * Starts the reload process, which includes a delay before resetting ammo.
     */
    private void startReload() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                onReload(); // Call onReload after delay
            }
        }, RELOAD_TIME); // Set delay time
    }

    /**

    /**
     * Resets the ammo count to 20 when reloading.
     */
    private void onReload() {
        currentAmmo = 20; // Reset ammo count
        weaponComponent.setAmmo(currentAmmo); // Update ammo in WeaponComponent
        updateAmmoDisplay(); // Update the UI accordingly
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
        pickaxeLabel.remove();
        shotgunLabel.remove();
        for (Image ammoImage : ammoImages) {
            ammoImage.remove();
        }
        ammoTable.clear();
    }
}
