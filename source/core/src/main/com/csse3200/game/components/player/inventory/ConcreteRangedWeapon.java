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
        Texture texture1;
        this.specification = specification;
        try{
            List<String> parts = Arrays.stream(specification.split(",")).toList();
            System.out.println(parts);
            // read the img path and create a texture
            this.name = parts.get(0);
            texture1 = new Texture(parts.get(1));
            this.texture = texture1;
            this.damage = Integer.parseInt(parts.get(2));
            this.range = Integer.parseInt(parts.get(3));
            this.fireRate = Integer.parseInt(parts.get(4));
            this.ammo = Integer.parseInt(parts.get(5));
            this.maxAmmo = Integer.parseInt(parts.get(6));
            this.reloadTime = Integer.parseInt(parts.get(7));
        } catch (Exception e) {
            // defaulting to shotgun
            throw new IllegalArgumentException("Invalid weapon spec: " + e);
            //texture1 = new Texture("images/Shotgun.png");
        }

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Texture getIcon() {
        return new Texture("Shotgun.png");
    }

    @Override
    public String getSpecification() {
        return super.getSpecification() + this.specification;
    }

    @Override
    public void shoot(Vector2 direction) {
        // Do nothing
    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
        Sprite weaponSprite = new Sprite(getIcon());
    }


}
