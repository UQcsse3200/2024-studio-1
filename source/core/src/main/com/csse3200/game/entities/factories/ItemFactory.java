package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.GoblinsGamble;
import com.csse3200.game.components.player.inventory.buffs.WerewolfFang;
import com.csse3200.game.components.player.inventory.buffs.*;
import com.csse3200.game.components.player.inventory.pets.RingFire;
import com.csse3200.game.components.player.inventory.pets.Tombstone;
import com.csse3200.game.components.player.inventory.usables.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory that creates usable items and buffs.
 */
public class ItemFactory {
    private static final Map<String, ItemCreator> creators = loadCreators();

    private static Map<String, ItemCreator> loadCreators() {
        Map<String, ItemCreator> creators = new HashMap<>();
        creators.put("medkit", (args) -> new MedKit());
        creators.put("bandage", (args) -> new Bandage());
        creators.put("energydrink", (args) -> new EnergyDrink(args[0]));
        creators.put("shieldpotion", (args) -> new ShieldPotion());
        creators.put("syringe", (args) -> new Syringe());
        creators.put("armor", (args) -> new Armor());
        creators.put("damagebuff", (args) -> new DamageBuff());
        creators.put("reroll", (args) -> new Reroll());
        creators.put("targetdummy", (args) -> new TargetDummy());
        creators.put("beartrap", (args) -> new BearTrap());
        creators.put("heart", (args) -> new Heart());
        creators.put("feather", (args) -> new Feather());
        creators.put("tombstone", (args) -> args.length < 1 ? new Tombstone()
                                                            : new Tombstone(args[0]));
        creators.put("ringfire", (args) -> new RingFire());
        creators.put("divinepotion", (args) -> new DivinePotion());
        creators.put("bigredbutton", (args) -> new BigRedButton());
        creators.put("teleporter", (args) -> new TeleporterItem());
        creators.put("goblinsgamble", (args) -> new GoblinsGamble());
        creators.put("fang", (args)-> new WerewolfFang());
        return creators;
    }

    /**
     * Creates a specified entity
     *
     * @param specification The specification of the item to create.
     * @return the specified entity
     */
    public Collectible create(String specification) {
        String[] arguments = specification.split(":");
        String type = arguments[0];


        arguments = Arrays.stream(Arrays.copyOfRange(arguments, 1, arguments.length))
                .filter(s -> !s.equals("mystery"))
                .toArray(String[]::new);

        ItemCreator creator = creators.getOrDefault(type, (args) -> {
            throw new IllegalArgumentException("Invalid item specification: " + specification);
        });

        return creator.create(arguments);
    }

    private interface ItemCreator {
        Collectible create(String[] arguments);
//
//        return switch (specification){
//            case "medkit" -> createMedKit();
//            case "bandage" -> createBandage();
//            case "energydrink" -> createEnergyDrink(type);
//            case "shieldpotion" -> createShieldPotion();
//            case "syringe" -> createSyringe();
//            case "armor" -> createArmor();
//            case "damagebuff" -> createDamageBuff();
//            case "reroll" -> createReroll();
//            case "targetdummy" -> createTargetDummy();
//            case "beartrap" -> createBearTrap();
//            case "heart" -> createHeart();
//            case "feather" -> createFeather();
//            case "tombstone" -> createTombstone();
//            case "ringfire" -> createRingFire();
//            case "divinepotion" -> createDivinePotion();
//            case "fang" -> createWerewolfFang();
//            case "bigredbutton" -> createBigRedButton();
//            case "teleporter" -> createTeleporter();
//            case "goblinsgamble" -> createGoblinsGamble();
//            default -> throw new IllegalArgumentException("Invalid item specification: " + specification);
//        };
    }

//    /**
//     * Creates a MedKit that restores players health.
//     * @return MedKit item
//     */
//    private Collectible createMedKit() {
//        return new MedKit();
//    }
//
//    private Collectible createReroll() {
//        return new Reroll();
//    }
//
//    /**
//     * Creates a Bandage that provides minimal health restoration to player.
//     * @return Bandage item
//     */
//    private Collectible createBandage() {
//        return new Bandage();
//    }
//
//    /**
//     * Creates an EnergyDrink that permanently increases the players movement speed.
//     * @return EnergyDrink item
//     */
//    private Collectible createEnergyDrink(String speedType) {
//        return new EnergyDrink(speedType);
//    }
//
//    /**
//     * Creates a Shield Potion that provides the player with temporary immunity against damage.
//     * @return ShieldPotion item
//     */
//    private Collectible createShieldPotion() {
//        return new ShieldPotion();
//    }
//
//    /**
//     * Creates a Syringe that provides player with an edge by exceeding their maximum health.
//     * @return Syringe item
//     */
//    private Collectible createSyringe() {
//        return new Syringe();
//    }
//
//    /**
//     * Creates an Armor item that increases the armor statistics of the player.
//     * @return Armor item
//     */
//    private Collectible createArmor() { return new Armor();}
//
//    /**
//     * Creates a damage buff item that increase the damage the player does
//     * @return damage buff item
//     */
//    private Collectible createDamageBuff() { return new DamageBuff();}
//
//    /**
//     * Creates a bleed buff item that does bleed damage to the enemies
//     * @return Bleed buff item
//     */
//    private Collectible createWerewolfFang() { return new WerewolfFang();}
//
//    /**
//     * Creates a target dummy item which spawns in a target dummy entity for enemies to attack
//     * @return TargetDummy item
//     */
//    private Collectible createTargetDummy() {
//        return new TargetDummy();
//    }
//
//    /**
//     * Creates a Bear trap that provides damage to the enemy entity upon collision.
//     * @return Bear Trap item
//     */
//    private Collectible createBearTrap() {
//        return new BearTrap();
//    }
//
//    /**
//     * Creates a Heart item that increases the player's maximum health
//     * @return Heart item
//     */
//    private Collectible createHeart() {
//        return new Heart();
//    }
//
//    /**
//     * Creates a Feather item that give the player the ability to crit hit enemies
//     * @return Feather item
//     */
//    private Collectible createFeather() {
//        return new Feather();
//    }
//
//    private Collectible createTombstone() {
//        return new Tombstone();
//    }
//    private Collectible createRingFire() {return new RingFire();}
//    /**
//     * Creates a divine potion item that increases the player's health and speed
//     * @return divine potion item
//     */
//    private Collectible createDivinePotion() {
//        return new DivinePotion();
//    }
//
//    /**
//     * Creates a big red button item that kills all enemies in the current room
//     * @return BigRedButton item
//     */
//    private Collectible createBigRedButton() {return new BigRedButton();}
//
//    /**
//     * Creates an item that teleports the player to the boss room
//     * @return teleporter item
//     */
//    private Collectible createTeleporter() {return new TeleporterItem();}
//
//    /**
//     * Creates a new goblins gamble item which the player can pick up to either win or lose coins
//     * @return GoblinsGamble item
//     */
//    private Collectible createGoblinsGamble() {
//        return new GoblinsGamble();
//
//    }
}

