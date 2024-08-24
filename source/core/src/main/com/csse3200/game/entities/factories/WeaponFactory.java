package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.*;

public class WeaponFactory {
    private MeleeWeapon createMelee(String specification) {
        // specification format: "melee:<Melee Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>"

        return new Knife();
    }

    private RangedWeapon createRanged(String specification){
        // specification format: "ranged:<Ranged Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>,<ammo>,<maxAmmo>,<reloadTime>"
        return new ConcreteRangedWeapon(specification);
    }

    public Collectible create(Collectible.Type type, String specification){
        return switch (type) {
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("invalid weapon type: " + type);
        };
    }
}
