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
        return switch (specification){
            case "medkit" -> createMedKit();
            case "bandage" -> createBandage();
            case "energydrink" -> createEnergyDrink();
            case "shieldpotion" -> createShieldPotion();
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
    private Collectible createEnergyDrink() {
        return new EnergyDrink();
    }

    /**
     * Creates a Shield Potion that provides the player with temporary immunity against damage.
     * @return ShieldPotion item
     */
    private Collectible createShieldPotion() {
        return new ShieldPotion();
    }

}

