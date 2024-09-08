// package com.csse3200.game.entities;

// import com.csse3200.game.components.player.inventory.Collectible;
// import com.csse3200.game.components.player.inventory.MeleeWeapon;
// import com.csse3200.game.components.player.inventory.RangedWeapon;
// import com.csse3200.game.entities.factories.WeaponFactory;
// import org.junit.Test;

// import static org.junit.Assert.assertThrows;
// import static org.junit.Assert.assertTrue;

// public class WeaponFactoryTest {
//     // Test that the WeaponFactory can create a MeleeWeapon (cover the commented area and ctrl+/ to remove comments)
// //    @Test
// //    public void testCreateMeleeWeapon() {
// //        WeaponFactory factory = new WeaponFactory();
// //        Collectible collectible = factory.create(Collectible.Type.MELEE_WEAPON, "melee:ConcreteMeleeWeapon,images/box_boy.png,10,20,30");
// //        assertTrue(collectible instanceof MeleeWeapon);
// //    }
// //
// //    @Test
// //    public void testCreateRangedWeapon() {
// //        WeaponFactory factory = new WeaponFactory();
// //        Collectible collectible = factory.create(Collectible.Type.RANGED_WEAPON, "ranged:ConcreteRangedWeapon,images/box_boy.png,10,20,30,40,50,60");
// //        assertTrue(collectible instanceof RangedWeapon);
// //    }

//     // @Test
//     // public void testCreateInvalidMeleeWeapon() {
//     //     WeaponFactory factory = new WeaponFactory();
//     //     assertThrows(IllegalArgumentException.class, () -> factory.create(Collectible.Type.MELEE_WEAPON, "melee:InvalidMeleeWeapon"));
//     // }

//     // // Test that the WeaponFactory can create a RangedWeapon
//     // @Test
//     // public void testCreateInvalidRangedWeapon() {
//     //     WeaponFactory factory = new WeaponFactory();
//     //     assertThrows(IllegalArgumentException.class, () -> factory.create(Collectible.Type.RANGED_WEAPON, "ranged:InvalidRangedWeapon"));
//     // }

//     // // Test that the WeaponFactory throws an IllegalArgumentException when an invalid weapon type is provided
//     // @Test
//     // public void testCreateInvalidWeaponType() {
//     //     WeaponFactory factory = new WeaponFactory();
//     //     assertThrows(IllegalArgumentException.class, () -> factory.create(Collectible.Type.ITEM, "invalid:InvalidWeapon"));
//     // }

//     // // Test that the WeaponFactory throws an IllegalArgumentException when an invalid weapon specification is provided
//     // @Test
//     // public void testCreateInvalidWeaponSpecification() {
//     //     WeaponFactory factory = new WeaponFactory();
//     //     assertThrows(IllegalArgumentException.class, () -> factory.create(Collectible.Type.RANGED_WEAPON, "ranged:ConcreteRangedWeapon,pathtoicon,10,20,30,40,50,60,70"));
//     // }

// }
