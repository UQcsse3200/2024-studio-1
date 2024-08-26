package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class Shotgun extends RangedWeapon {

    private Texture texture;

    public Shotgun() {
        int damage = 30;
        int range = 5;
        int fireRate = 5;
        int ammo = 10;
        int maxAmmo = 20;
        int reloadTime = 5;
        setDamage(damage);
        setRange(range);
        setFireRate(fireRate);
        setAmmo(ammo);
        setMaxAmmo(maxAmmo);
        setReloadTime(reloadTime);

        // Load the shotgun texture
        texture = new Texture(Gdx.files.internal("images/Weapons/Shotgun.png"));
    }

    @Override
    public void shoot(Vector2 direction) {
        if (getAmmo() > 0) {
            // Implement the shooting logic for the shotgun
            System.out.println("Shotgun fired in direction: " + direction);
            setAmmo(getAmmo() - 1); // Decrease ammo count after shooting
        } else {
            System.out.println("Out of ammo! Need to reload.");
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        // Dispose of the texture when no longer needed to free resources
        texture.dispose();
    }

    @Override
    public String getName() {
        return "shotgun";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/Weapons/Shotgun.png");
    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }
}
