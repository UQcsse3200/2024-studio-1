package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ConcreteRangedWeapon extends RangedWeapon {

    private static final Logger logger = LoggerFactory.getLogger(ConcreteRangedWeapon.class);
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
        return new Texture("images/Weapons/Shotgun.png");
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
        logger.info("Picking up ranged weapon - no entity");
        super.pickup(inventory);
        Sprite weaponSprite = new Sprite(getIcon());
    }

    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        logger.info("Picking up ranged weapon - with entity");
        super.pickup(inventory, itemEntity);
        Sprite weaponSprite = new Sprite(getIcon());
    }


}
