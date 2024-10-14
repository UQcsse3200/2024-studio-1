package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A UI component for displaying player stats, e.g., health, weapon, ammo.
 */
public class PlayerStatsDisplay extends UIComponent {
    Table table;
    Table ammoTable;
    private Image heartImage;
    private Label healthLabel;
    private Label swordLabel;
    private Label shotgunLabel;  // Separate label for shotgun
    private Label pistolLabel;   // New label for pistol

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

    private ArrayList<Label> labels;
    public static final String HEART_TEXTURE = "images/heart.png";
    public static final String SPEED_TEXTURE = "images/items/energy_drink.png";
    public static final String DAMAGE_BUFF_TEXTURE = "images/items/damage_buff.png";

    /**
     * Creates reusable UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        ammoImages = new ArrayList<>();
        addActors();

        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("melee_pickup", this::updateMeleeWeaponUI);
        entity.getEvents().addListener("ranged_pickup", (Integer maxAmmo) -> {
            updateShotgunLabel(20, "Shotgun");
            updateRangedWeaponUI(maxAmmo, "Shotgun");
        });
        entity.getEvents().addListener("ranged_activate", this::updateAmmoDisplay);
        entity.getEvents().addListener("updateSpeedPercentage", this::updateSpeedPercentageUI);
        entity.getEvents().addListener("updateDamageBuff", this::updateDamageUI);
        entity.getEvents().addListener("updateSpeedUI", this::updateSpeedPercentageUI);

        initializeWeaponsAndAmmo();
    }

    /**
     * Initializes the weapon and ammo counts based on the player selected.
     * Retrieves the player type from PlayerAnimationController.
     */
    private void initializeWeaponsAndAmmo() {
        // Retrieve the PlayerAnimationController component from the entity
        PlayerAnimationController playerController = entity.getComponent(PlayerAnimationController.class);

        // Get the current player number
        PlayerAnimationController.PlayerNum playerNum = playerController.getPlayerNum();

        // Based on the selected player, display appropriate weapon and ammo counts
        if (playerNum == PlayerAnimationController.PlayerNum.PLAYER_4 ||
                playerNum == PlayerAnimationController.PlayerNum.PLAYER_2 ||
                playerNum == PlayerAnimationController.PlayerNum.PLAYER_3 ||
                playerNum == PlayerAnimationController.PlayerNum.BEAR) {
            // Player 4 has a pistol with 7 ammo and no shotgun
            updatePistolLabel(1, "Pistol");
            updateRangedWeaponUI(7, "Pistol");
            updateShotgunLabel(0, "Shotgun");  // Shotgun ammo is set to 0
        } else {
            // For other players, set the shotgun to have 20 ammo
            updateShotgunLabel(1, "Shotgun");
            updateRangedWeaponUI(20, "Shotgun");
            updatePistolLabel(0, "Pistol"); // No pistol for other players
        }
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        labels = new ArrayList<>();

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

        // Speed image
        float speedSideLength = 100f;
        speedImage = new Image(ServiceLocator.getResourceService().getAsset(SPEED_TEXTURE, Texture.class));

        // Speed progress bar
        speedProgressBar = new ProgressBar(0f, 1.5f, 0.1f, false, skin);
        speedProgressBar.setWidth(200f);
        speedProgressBar.setAnimateDuration(2.0f);

        // Damage progress bar
        float damageSideLength = 50f;
        damageImage = new Image(ServiceLocator.getResourceService().getAsset(DAMAGE_BUFF_TEXTURE, Texture.class));
        damageProgressBar = new ProgressBar(0f, 5.0f, 0.1f, false, skin);
        damageProgressBar.setWidth(200f);
        damageProgressBar.setAnimateDuration(2.0f);

        // Weapon labels
        swordLabel = new Label("Sword: 0", skin, "small");
        shotgunLabel = new Label("Shotgun: 0", skin, "small");  // Shotgun label
        pistolLabel = new Label("Pistol: 0", skin, "small");    // New Pistol label

        table.add(heartImage).size(heartSideLength).pad(5);
        table.add(healthLabel).padLeft(10).left();
        labels.add(healthLabel);

        table.row().padTop(10);
        table.add(speedImage).size(speedSideLength).pad(5);
        table.add(speedProgressBar).padLeft(10).left().width(200);

        table.row().padTop(10);
        table.add(damageImage).size(damageSideLength).pad(5);
        table.add(damageProgressBar).padLeft(10).left().width(200);

        table.row().padTop(10);
        table.add(swordLabel).colspan(2).padLeft(10).left();
        labels.add(swordLabel);

        // Add both shotgun and pistol labels
        table.row().padTop(10);
        table.add(shotgunLabel).colspan(2).padLeft(10).left();
        table.row().padTop(10);
        table.add(pistolLabel).colspan(2).padLeft(10).left();

        table.row().padTop(10);
        table.add(ammoTable).colspan(2).left().padLeft(2);
        stage.addActor(table);
    }

    public void resize(int width, int height) {
        if (labels != null) {
            for (Label label : labels) {
                label.setFontScale(width / 1100f);
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Drawing handled automatically by the stage and actors
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

    public void updateMeleeWeaponUI() {
        CharSequence text = String.format("Sword x %d", 1);
        swordLabel.setText(text);
    }

    /**
     * Updates the ranged weapon UI.
     * If the player is Player 4, show the pistol with 7 ammo.
     *
     * @param maxAmmo the maximum ammo count for the weapon.
     * @param weaponName the name of the weapon (e.g., "Shotgun" or "Pistol").
     */
    public void updateRangedWeaponUI(int maxAmmo, String weaponName) {
        currentAmmo = maxAmmo;  // Initialize ammo count from WeaponComponent
        displayAllAmmo();

        CharSequence text = String.format("%s x %d", weaponName, 1);
        if (weaponName.equals("Pistol")) {
            pistolLabel.setText(text); // Update pistol label
        } else {
            shotgunLabel.setText(text); // Update shotgun label
        }
    }

    /**
     * Updates the shotgun label with the specified ammo count.
     */
    public void updateShotgunLabel(int maxAmmo, String weaponName) {
        CharSequence text = String.format("%s x %d", weaponName, maxAmmo);
        shotgunLabel.setText(text);
    }

    /**
     * Updates the pistol label with the specified ammo count.
     */
    public void updatePistolLabel(int maxAmmo, String weaponName) {
        CharSequence text = String.format("%s x %d", weaponName, maxAmmo);
        pistolLabel.setText(text);
    }

    /**
     * Displays ammo based on the count of the ranged weapon.
     * This method is called when the weapon's ammo count is greater than 0.
     * Displays ammo in rows within a separate ammo table.
     */
    private void displayAllAmmo() {
        ammoTable.clear(); // Clear existing ammo from the ammoTable
        ammoTable.top().left(); // Reset alignment
        int itemsInRow = 0;

        // Always display ammo up to currentAmmo
        for (int i = 0; i < currentAmmo; i++) {
            if (itemsInRow == 5) {
                ammoTable.row().padTop(2f); // Start a new row after 5 ammo items
                itemsInRow = 0;
            }
            ammoImages.get(i).setVisible(true);
            ammoTable.add(ammoImages.get(i)).size(screenWidth / 45f);
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
     * Updates the player's speed on the UI.
     *
     * @param speedPercentage the player's new speed percentage to update the UI to
     */
    public void updateSpeedPercentageUI(float speedPercentage, String speedType) {
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
