package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;

public class WeaponFactory {
    private MeleeWeapon createMelee() {
        return null;
    }

    private RangedWeapon createRanged(){
        return null;
    }

    public Collectible create(Collectible.Type type, String specification){
        return switch (type) {
            case MELEE_WEAPON -> createMelee();
            case RANGED_WEAPON -> createRanged();
            default -> null;
        };
    }
}
