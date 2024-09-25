package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;

public class MeleeWeapon implements Collectible {

    private String name;
    private String iconPath;
    private int damage;
    private int range;
    private int fireRate;

    public MeleeWeapon(String name, String iconPath, int damage, int range, int fireRate) {
        this.name = name;
        this.iconPath = iconPath;
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
    }

    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setMelee(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this); // update existing weapon
        }
    }
    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        inventory.setMelee(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this,
                    itemEntity);
            // update existing weapon
        }
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();

        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).dropMeleeWeapon(); // remove weapon
        }
    }

    @Override
    public String getSpecification() {
        return "melee:" + name;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Texture getIcon() {
        return new Texture(iconPath);
    }

}
