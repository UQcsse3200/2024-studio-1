package com.csse3200.game.entities.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeaponConfig {

    private static final Logger logger = LoggerFactory.getLogger(WeaponConfig.class);

    // Store weapon data in a map
    private static final Map<String, WeaponData> weaponConfigs = new HashMap<>();

    static {
        // Load weapon data (can be from JSON or hardcoded for now)
        weaponConfigs.put("shotgun", new WeaponData("shotgun", 10, 10,
                5, 20, 20, 3, "images" +
                "/Weapons/Centered/Shotgun.png"));
        weaponConfigs.put("plasmablaster", new WeaponData("plasmaBlaster", 10,
                10, 5, 20, 20, 3,
                "images" + "/Weapons/Centered/PlasmaBlaster.png"));
        weaponConfigs.put("supersoaker", new WeaponData("superSoaker", 10,
                10, 5, 20, 20, 3,
                "images" + "/Weapons/Centered/SuperSoaker.png"));
        weaponConfigs.put("fnscar", new WeaponData("fnScar", 10,
                10, 5, 20, 20, 3,
                "images" + "/Weapons/Centered/FnScar.png"));

        weaponConfigs.put("knife", new WeaponData("knife", 30,
                4, 0, "images/Weapons/Centered" +
                "/Knife" +
                ".png"));
        weaponConfigs.put("axe", new WeaponData("axe", 50,
                10, 1, "images/Weapons/Centered/Axe.png"));

    }

    // Method to fetch weapon data by specification
    public static WeaponData getWeaponData(String specification) {
        return weaponConfigs.get(specification);
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
