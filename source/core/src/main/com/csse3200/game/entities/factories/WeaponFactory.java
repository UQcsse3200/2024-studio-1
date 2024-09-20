package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(WeaponFactory.class);

    private MeleeWeapon createMelee(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid melee weapon specification: " + specification);
        }

        MeleeWeapon meleeWeapon = new MeleeWeapon() {
            {
                setDamage(weaponData.getDamage());
                setRange(weaponData.getRange());
                setFireRate(weaponData.getFireRate());
            }

            @Override
            public String getName() {
                return specification;
            }

            @Override
            public String getMeleeSpecification() {
                return specification;
            }

            @Override
            public Texture getIcon() {
                return new Texture(weaponData.getIconPath());  // Fetch icon from config
            }
        };
        return meleeWeapon;
    }

    private RangedWeapon createRanged(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid ranged weapon specification: " + specification);
        }

        RangedWeapon rangedWeapon = new RangedWeapon() {
            {
                setDamage(weaponData.getDamage());
                setRange(weaponData.getRange());
                setFireRate(weaponData.getFireRate());
                setAmmo(weaponData.getAmmo());
                setMaxAmmo(weaponData.getMaxAmmo());
                setReloadTime(weaponData.getReloadTime());
            }

            @Override
            public String getName() {
                return specification;
            }

            @Override
            public Texture getIcon() {
                return new Texture(weaponData.getIconPath());  // Fetch icon from config
            }

            @Override
            public String getRangedSpecification() {
                return specification;
            }

            @Override
            public void shoot(Vector2 direction) {
                if (getAmmo() > 0) {
                    System.out.println(specification + " fired in direction: " + direction);
                    setAmmo(getAmmo() - 1);
                } else {
                    System.out.println("Out of ammo! Need to reload.");
                }
            }
        };
        return rangedWeapon;
    }

    public Collectible create(Collectible.Type type, String specification) throws IllegalArgumentException {
        return switch (type) {
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("Invalid weapon type: " + type);
        };
    }

    public Entity createWeaponEntity(Collectible collectible) throws IllegalArgumentException {
        try {
            if (collectible.getType() == Collectible.Type.MELEE_WEAPON) {
                return WeaponConfig.createMeleeEntity((MeleeWeapon) collectible);
            }
            return WeaponConfig.createRangeEntity((RangedWeapon) collectible);
        } catch (Exception e) {
            logger.error("Failed to create weapon entity: {}", e.toString());
            throw new IllegalArgumentException("Invalid collectible");
        }
    }
    /**
     * Get all the filepath to textures needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Weapons/sword1.png",
                "images/Weapons/shotgun4.png",
                "images/Weapons/shotgun_1.png",
                "images/Weapons/shotgun_2.png",
                "images/Weapons/winchester.png"
        };
    }

    /**
     * Get all the filepath to sounds needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/shotgun1_f.ogg",
                "sounds/shotgun1_r.ogg",
                "sounds/sword1.ogg"
        };
    }

    /**
     * Get all the filepath to texture atlases needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getTextureAtlasFilepaths(){
        return new String[]{
                "images/Weapons/sword1.atlas",
                "images/Weapons/shotgun4.atlas",
                "images/Weapons/shotgun_1.atlas",
                "images/Weapons/shotgun_2.atlas",
                "images/Weapons/shotgun_3.atlas",
                "images/Weapons/50_cal.atlas",
                "images/Weapons/ak.atlas",
                "images/Weapons/fn_scar.atlas",
                "images/Weapons/super_soaker.atlas",
                "images/Weapons/winchester.atlas"
        };
    }
}
