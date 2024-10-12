package com.csse3200.game.components.player.inventory.pets;


import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.Pet;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * A Tombstone is a pet item that resurrects an animal under your control.
 */
public class Tombstone extends Pet {
    private final String petName;

    /**
     * A tombstone for a specific pet.
     *
     * @param petName the name of the pet this tombstone spawns.
     */
    public Tombstone(String petName) {
        this.petName = petName;
    }

    /**
     * A Tombstone for a randomly selected pet.
     */
    public Tombstone() {
        this(selectPet());
    }

    /**
     * Returns the string of the tombstone item
     *
     * @return string of the item
     */
    @Override
    public String getPetSpecification() {
        return "tombstone:" + petName;
    }

    /**
     * Spawns pet entity to assist player
     *
     * @param entity The player entity
     */
    @Override
    public Entity spawn(Entity entity) {
        Entity newPet = new PetFactory().create(this.petName);

        ServiceLocator.getEntityService().register(newPet);
        newPet.setPosition(5, 7);

        return newPet;
    }

    /**
     * Uses a random number generator to select a pet.
     *
     * @return the name of the chosen pet.
     */
    private static String selectPet() {
        int selected = ServiceLocator.getRandomService()
                .getRandomNumberGenerator(Tombstone.class)
                .getRandomInt(1, 6);

        return switch (selected) {
            case 2 -> "Bear";
            case 3 -> "Snake";
            case 4 -> "Bat";
            case 5 -> "Dog";
            case 6 -> "Minotaur";
            default -> "Rat";
        };
    }

    /**
     * Gets the name of the item
     *
     * @return the name of the item
     */
    @Override
    public String getName() {
        return delegate != null ? delegate.getName() : "Tombstone";
    }

    /**
     * Gets the render for the item
     *
     * @return the items image
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/tombstone.png");
    }
}
