package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * Example Weapon Collectible.
 */
public class ConcreteMeleeWeapon extends MeleeWeapon {
    private final String specification;
    private final String name;
    private final Texture texture;
    private final int damage;
    private final int range;
    private final int fireRate;

    public ConcreteMeleeWeapon(String specification) {
        Texture texture1;
        this.specification = specification;
        try {
            List<String> parts = Arrays.stream(specification.split(",")).toList();
            System.out.println(parts);
            // read the img path and create a texture
            this.name = parts.get(0);
            texture1 = new Texture(parts.get(1));
            this.texture = texture1;
            this.damage = Integer.parseInt(parts.get(2));
            this.range = Integer.parseInt(parts.get(3));
            this.fireRate = Integer.parseInt(parts.get(4));
        } catch (Exception e) {
            // defaulting to knife
            throw new IllegalArgumentException("Invalid weapon specification: " + e);
            //texture1 = new Texture("knife.png");
        }
    }

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }
    public void pickup(Inventory inventory, Entity itemEntity) {
        super.pickup(inventory, itemEntity);
    }


    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    @Override
    public String getMeleeSpecification() {
        return this.specification;
    }

    @Override
    public String getName() {
        return "knife";
    }

    @Override
    public Texture getIcon() {
        return new Texture("pickaxe.png");
    }
}
