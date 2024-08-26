package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;

import java.util.Arrays;
import java.util.List;

public class ConcreteRangedWeapon extends RangedWeapon {
    private final String specification;
    private final String name;
    private final Texture texture;

    private final int damage;
    private final int range;
    private final int fireRate;
    private final int ammo;
    private final int maxAmmo;
    private final int reloadTime;


    public ConcreteRangedWeapon(String specification) {
        this.specification = specification;
        List<String> parts = Arrays.stream(specification.split(",")).toList();

        this.name = parts.getFirst();
        this.texture = new Texture(parts.get(1));
        this.damage = Integer.parseInt(parts.get(2));
        this.range = Integer.parseInt(parts.get(3));
        this.fireRate = Integer.parseInt(parts.get(4));
        this.ammo = Integer.parseInt(parts.get(5));
        this.maxAmmo = Integer.parseInt(parts.get(6));
        this.reloadTime = Integer.parseInt(parts.get(7));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Texture getIcon() {
        return this.texture;
    }

    @Override
    public String getRangedSpecification() {
        return this.specification;
    }

    @Override
    public void shoot(Vector2 direction) {
        // Do nothing
    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
        Sprite weaponSprite = new Sprite(getIcon());
        inventory.getEntity().addComponent(new WeaponComponent(
                weaponSprite,
                WeaponComponent.WeaponType.SHOTGUN,
                this.damage,
                this.range,
                this.fireRate,
                this.ammo,
                this.maxAmmo,
                this.reloadTime
        ));
    }


}
