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
        String speedType = null;
        if (specification.contains(":")) {
            String[] split = specification.split(":", 2);
            specification = split[0];
            speedType = split[1];
            //check valid speedType
        }
        return switch (specification){
            case "medkit" -> createMedKit();
            case "bandage" -> createBandage();
            case "energydrink" -> createEnergyDrink(speedType);
            case "shieldpotion" -> createShieldPotion();
            case "syringe" -> createSyringe();
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

}

