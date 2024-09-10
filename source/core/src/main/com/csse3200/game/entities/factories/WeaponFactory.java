package com.csse3200.game.entities.factories;
import com.csse3200.game.components.player.inventory.*;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory extends LoadedFactory {
    private MeleeWeapon createMelee(String specification) {
        return switch (specification) {
            case "knife" -> new Knife();
            case "pickaxe" -> new Pickaxe();
            default -> new ConcreteMeleeWeapon(specification);
        };
    }

    private RangedWeapon createRanged(String specification){
        // specification format: "ranged:<Ranged Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>,<ammo>,<maxAmmo>,<reloadTime>"
        if (specification.equals("shotgun")) {
            return new Shotgun();
        }
        System.out.println(specification);
        return new ConcreteRangedWeapon(specification);
    }

    /**
     * Create a new weapon from a specification.
     * @param type the type of the weapon (melee or ranged)
     * @param specification the specification of the weapon.
     * @return The newly constructed collectible.
     */
    public Collectible create(Collectible.Type type, String specification) {
        return switch (type) {
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("invalid weapon type: " + type);
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Weapons/Shotgun.png",
                "images/Weapons/pickaxe.png"
        };
    }
    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/shotgun1_f.ogg",
                "sounds/shotgun1_r.ogg"
        };
    }
}