package com.csse3200.game.entities.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeaponConfig {
    private static final Logger logger = LoggerFactory.getLogger(WeaponConfig.class);

    // Store weapon data in a map
    private static final Map<String, WeaponData> WEAPON_CONFIGS = new HashMap<>();

    static {
        // Load weapon data (can be from JSON or hardcoded for now)
        WEAPON_CONFIGS.put("shotgun", new WeaponData("shotgun", 12, 5,
                3, 6, 6, 1, "images/Weapons/Centered/shotgun.png"));
        WEAPON_CONFIGS.put("plasmablaster", new WeaponData("plasmablaster", 7,
                6, 5, 20, 20, 2,
                "images/Weapons/Centered/plasmablaster.png"));
        WEAPON_CONFIGS.put("supersoaker", new WeaponData("supersoaker", 15,
                6, 4, 12, 12, 3,
                "images/Weapons/Centered/supersoaker.png"));
        WEAPON_CONFIGS.put("fnscar", new WeaponData("fnscar", 28,
                0, 3, 20, 20, 3,
                "images" + "/Weapons/Centered/fnscar.png"));
        WEAPON_CONFIGS.put("pistol", new WeaponData("pistol", 20,
                0, 4, 15, 15, 1,
                "images" + "/Weapons/Centered/pistol.png"));
        WEAPON_CONFIGS.put("knife", new WeaponData("knife", 20,
                4, 3, "images/Weapons/Centered/knife.png"));
        WEAPON_CONFIGS.put("axe", new WeaponData("axe", 80,
                6, 1, "images/Weapons/Centered/axe.png"));

    }

    // Method to fetch weapon data by specification
    public static WeaponData getWeaponData(String specification) {
        return WEAPON_CONFIGS.get(specification);
    }

    // Define the WeaponData class
    public static class WeaponData {

        private final String name;
        private final int damage;
        private final int range;
        private final int fireRate;
        private final int ammo;
        private final int maxAmmo;
        private final int reloadTime;
        private final String iconPath;

        // Constructor for ranged weapons
        public WeaponData(String name, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime, String iconPath) {
            this.name = name;
            this.damage = damage;
            this.range = range;
            this.fireRate = fireRate;
            this.ammo = ammo;
            this.maxAmmo = maxAmmo;
            this.reloadTime = reloadTime;
            this.iconPath = iconPath;
        }

        // Constructor for melee weapons
        public WeaponData(String name, int damage, int range, int fireRate, String iconPath) {
            this(name, damage, range, fireRate, 0, 0, 0, iconPath);
        }

        public String getName() {
            return name;
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

        public int getAmmo() {
            return ammo;
        }

        public int getMaxAmmo() {
            return maxAmmo;
        }

        public int getReloadTime() {
            return reloadTime;
        }

        public String getIconPath() {
            return iconPath;
        }
    }
}
