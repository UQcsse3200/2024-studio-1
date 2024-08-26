package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.csse3200.game.components.player.WeaponComponent;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeaponComponentTest {
    @Test
    public void testWeaponComponent() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component is created
        assertNotNull(weaponComponent);
    }

    @Test
    public void testWeaponComponentDefault() {
        // create a weapon component with default values
        WeaponComponent weaponComponent = new WeaponComponent(null, null, 0, 0, 0, 0, 0, 0);
        // check if weapon component is created
        assertNotNull(weaponComponent);
    }

    @Test
    public void testWeaponComponentDamage() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component damage is correct
        assertEquals(10, weaponComponent.getDamage());
    }

    @Test
    public void testWeaponComponentRange() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component range is correct
        assertEquals(5, weaponComponent.getRange());
    }

    @Test
    public void testWeaponComponentFireRate() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component fire rate is correct
        assertEquals(1, weaponComponent.getFireRate());
    }

    @Test
    public void testWeaponComponentAmmo() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component ammo is correct
        assertEquals(0, weaponComponent.getAmmo());
    }

    @Test
    public void testWeaponComponentMaxAmmo() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component max ammo is correct
        assertEquals(0, weaponComponent.getMaxAmmo());
    }

    @Test
    public void testWeaponComponentReloadTime() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component reload time is correct
        assertEquals(0, weaponComponent.getReloadTime());
    }

    @Test
    public void testWeaponComponentWeaponType() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component weapon type is correct
        assertEquals(Collectible.Type.MELEE_WEAPON, weaponComponent.getWeaponType());
    }

    @Test
    public void testWeaponComponentSetWeaponType() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon type to RANGED
        weaponComponent.setWeaponType(Collectible.Type.RANGED_WEAPON);
        // check if weapon component weapon type is correct
        assertEquals(Collectible.Type.RANGED_WEAPON, weaponComponent.getWeaponType());
    }

    @Test
    public void testWeaponComponentSetWeaponTypeNull() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon type to null
        weaponComponent.setWeaponType(null);
        // check if weapon component weapon type is correct
        assertNull(weaponComponent.getWeaponType());
    }

    @Test
    public void testWeaponComponentWeaponSprite() {
        // create a weapon component
        Sprite sprite = new Sprite();
        WeaponComponent weaponComponent = new WeaponComponent(sprite, Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component weapon sprite is correct
        assertEquals(sprite, weaponComponent.getWeaponSprite());
    }

    @Test
    public void testWeaponComponentWeaponSpriteNull() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(null, Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // check if weapon component weapon sprite is correct
        assertNull(weaponComponent.getWeaponSprite());
    }

    @Test
    public void testWeaponComponentWeaponSpriteSet() {
        // create a weapon component
        Sprite sprite = new Sprite();
        WeaponComponent weaponComponent = new WeaponComponent(null, Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon sprite
        weaponComponent.setWeaponSprite(sprite);
        // check if weapon component weapon sprite is correct
        assertEquals(sprite, weaponComponent.getWeaponSprite());
    }

    @Test
    public void testWeaponComponentSetDamage() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon damage to 20
        weaponComponent.setDamage(20);
        // check if weapon component damage is correct
        assertEquals(20, weaponComponent.getDamage());
    }

    @Test
    public void testWeaponComponentSetRange() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon range to 10
        weaponComponent.setRange(10);
        // check if weapon component range is correct
        assertEquals(10, weaponComponent.getRange());
    }

    @Test
    public void testWeaponComponentSetFireRate() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon fire rate to 2
        weaponComponent.setFireRate(2);
        // check if weapon component fire rate is correct
        assertEquals(2, weaponComponent.getFireRate());
    }

    @Test
    public void testWeaponComponentSetAmmo() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon ammo to 10
        weaponComponent.setAmmo(10);
        // check if weapon component ammo is correct
        assertEquals(10, weaponComponent.getAmmo());
    }

    @Test
    public void testWeaponComponentSetAmmoMinusOne() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon ammo to -1
        int ammo = weaponComponent.getAmmo();
        weaponComponent.setAmmo(-1);
        // check if weapon component ammo is correct
        assertEquals(ammo -1, weaponComponent.getAmmo());
    }

    @Test
    public void testWeaponComponentSetAmmoMinusTwo() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon ammo to -2
        weaponComponent.setAmmo(-2); // set to max ammo
        // check if weapon component ammo is correct
        assertEquals(weaponComponent.getMaxAmmo(), weaponComponent.getAmmo());
    }

    @Test
    public void testWeaponComponentSetMaxAmmo() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon max ammo to 10
        weaponComponent.setMaxAmmo(10);
        // check if weapon component max ammo is correct
        assertEquals(10, weaponComponent.getMaxAmmo());
    }

    @Test
    public void testWeaponComponentSetReloadTime() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon reload time to 10
        weaponComponent.setReloadTime(10);
        // check if weapon component reload time is correct
        assertEquals(10, weaponComponent.getReloadTime());
    }

    @Test
    public void testWeaponComponentSetReloadTimeNegative() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon reload time to -10
        try {
            weaponComponent.setReloadTime(-10);
        } catch (IllegalArgumentException e) {
            assertEquals("Reload time must be greater than 0", e.getMessage());
        }
    }

    @Test
    public void testWeaponComponentSetReloadTimeZero() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon reload time to 0
        weaponComponent.setReloadTime(0);
        // check if weapon component reload time is correct
        assertEquals(0, weaponComponent.getReloadTime());
    }

    @Test
    public void testWeaponComponentSetReloadTimeMaxValue() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.MELEE_WEAPON, 10, 5, 1, 0, 0, 0);
        // set weapon reload time to max value
        weaponComponent.setReloadTime(Integer.MAX_VALUE);
        // check if weapon component reload time is correct
        assertEquals(Integer.MAX_VALUE, weaponComponent.getReloadTime());
    }

    /*
    @Test
    public void testWeaponComponentAttack() {
        // create a weapon component
        WeaponComponent weaponComponent = new WeaponComponent(new Sprite(), Collectible.Type.RANGED_WEAPON, 10, 5, 1, 0, 0, 0);
        // attack with weapon without ammo
        int ammo = weaponComponent.getAmmo();
        weaponComponent.attack();
        // check if weapon component ammo is correct
        assertEquals(0, weaponComponent.getAmmo());
        // set weapon ammo to 10
        weaponComponent.setAmmo(10);
        // attack with weapon with ammo
        assertEquals(10, ammo);
        weaponComponent.attack();
        // check if weapon component ammo is correct
        assertEquals(ammo - 1, weaponComponent.getAmmo());
    }
    */
}
