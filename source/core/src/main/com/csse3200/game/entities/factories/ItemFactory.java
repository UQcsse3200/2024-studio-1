package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.*;

/**
 * A factory that creates usable items and buffs.
 */
public class ItemFactory {

    /**
     * Creates a specified entity
     *
     * @param specification The specification of the item to create.
     * @return the specified entity
     */
    public Collectible create(String specification) {
        String type = null;
        if (specification.contains(":")) {
            //This code if mainly for the speed boost item, however, could be used for other items that have a
            //"Low", "Medium" or "High" type
            String[] split = specification.split(":", 2);
            specification = split[0];
            if (specification.equals("energydrink")) {
                // If split contains mystery disregard it and get correct item type
                if (split[1].contains(":")) {
                    String[] secondSplit = split[1].split(":", 2);
                    type = secondSplit[0];
                } else {
                    type = split[1];
                }
                //Check for valid item type
                switch (type) {
                    case "Low":
                        break;
                    case "Medium":
                        break;
                    case "High":
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid item type specification: " + type);
                }
            }
        }
        if (specification.contains("energydrink") && type == null) {
            //Throw an error if energy drink item does not have an associated type
            throw new IllegalArgumentException("Energy drink must have a type. Choose: Low, Medium, High");
        }

        return switch (specification){
            case "medkit" -> createMedKit();
            case "bandage" -> createBandage();
            case "energydrink" -> createEnergyDrink(type);
            case "shieldpotion" -> createShieldPotion();
            case "syringe" -> createSyringe();
            case "armor" -> createArmor();
            case "damagebuff" -> createDamageBuff();
            case "reroll" -> createReroll();
            case "targetdummy" -> createTargetDummy();
            case "beartrap" -> createBearTrap();
            case "heart" -> createHeart();
            case "feather" -> createFeather();
            case "divinepotion" -> createDivinePotion();
            default -> throw new IllegalArgumentException("Invalid item specification: " + specification);
        };
    }

    /**
     * Creates a MedKit that restores players health.
     * @return MedKit item
     */
    private Collectible createMedKit() {
        return new MedKit();
    }

    private Collectible createReroll() {
        return new Reroll();
    }

    /**
     * Creates a Bandage that provides minimal health restoration to player.
     * @return Bandage item
     */
    private Collectible createBandage() {
        return new Bandage();
    }

    /**
     * Creates an EnergyDrink that permanently increases the players movement speed.
     * @return EnergyDrink item
     */
    private Collectible createEnergyDrink(String speedType) {
        return new EnergyDrink(speedType);
    }

    /**
     * Creates a Shield Potion that provides the player with temporary immunity against damage.
     * @return ShieldPotion item
     */
    private Collectible createShieldPotion() {
        return new ShieldPotion();
    }

    /**
     * Creates a Syringe that provides player with an edge by exceeding their maximum health.
     * @return Syringe item
     */
    private Collectible createSyringe() {
        return new Syringe();
    }

    /**
     * Creates an Armor item that increases the armor statistics of the player.
     * @return Armor item
     */
    private Collectible createArmor() { return new Armor();}

    /**
     * Creates a damage buff item that increase the damage the player does
     * @return damage buff item
     */
    private Collectible createDamageBuff() { return new DamageBuff();}

    /**
     * Creates a target dummy item which spawns in a target dummy entity for enemies to attack
     * @return TargetDummy item
     */
    private Collectible createTargetDummy() {
        return new TargetDummy();
    }

    /**
     * Creates a Bear trap that provides damage to the enemy entity upon collision.
     * @return Bear Trap item
     */
    private Collectible createBearTrap() {
        return new BearTrap();
    }

    /**
     * Creates a Heart item that increases the player's maximum health
     * @return Heart item
     */
    private Collectible createHeart() {
        return new Heart();
    }

    /**
     * Creates a Feather item that give the player the ability to crit hit enemies
     * @return Feather item
     */
    private Collectible createFeather() {
        return new Feather();
    }

    /**
     * Creates a divine potion item that increases the player's health and speed
     * @return divine potion item
     */
    private Collectible createDivinePotion() {
        return new DivinePotion();
    }
}

