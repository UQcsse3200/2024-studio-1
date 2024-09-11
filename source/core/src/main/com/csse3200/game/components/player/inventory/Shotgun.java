package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

public class Shotgun extends RangedWeapon {

    public Shotgun() {
        int damage = 30;
        int range = 5;
        int fireRate = 5;
        int ammo = 20;
        int maxAmmo = 20;
        int reloadTime = 3;
        setDamage(damage);
        setRange(range);
        setFireRate(fireRate);
        setAmmo(ammo);
        setMaxAmmo(maxAmmo);
        setReloadTime(reloadTime);
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
    public void pickup(Inventory inventory, Entity itemEntity) {
        super.pickup(inventory, itemEntity);
    }

    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    @Override
    public String getRangedSpecification() {
        return "shotgun";
    }
}
