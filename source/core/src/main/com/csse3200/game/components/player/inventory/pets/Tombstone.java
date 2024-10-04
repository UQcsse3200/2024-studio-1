package com.csse3200.game.components.player.inventory.pets;


import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.Pet;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.Random;


public class Tombstone extends Pet {
    Random random = new Random();

    /**
     * Returns the string of the tombstone item
     *
     * @return string of the item
     */
    @Override
    public String getPetSpecification() {
        return "tombstone";
    }


    /**
     * Spawns pet entity to assist player
     *
     * @param entity The player entity
     */
    @Override
    public Entity spawn(Entity entity) {
        int randomInt = this.random.nextInt(1, 5);

        Entity newPet = this.randomPetGenerator(randomInt);
        ServiceLocator.getEntityService().register(newPet);
        newPet.setPosition(5, 7);

        return newPet;
    }

    /**
     * Uses a random number generator to spawn a pet entity
     *
     * @param randomNum Random number generated from effect()
     * @return creates pet entity
     */
    private Entity randomPetGenerator(int randomNum) {
        String specification = "Rat";
        switch (randomNum) {
            case 1 -> specification = "Rat";
            case 2 -> specification = "Bear";
            case 3 -> specification = "Snake";
            case 4 -> specification = "Bat";
            case 5 -> specification = "Dog";
            case 6 -> specification = "Minotaur";
        }
        return new PetFactory().create(specification);
    }

    /**
     * Gets the name of the item
     *
     * @return the name of the item
     */
    @Override
    public String getName() {
        return "Tombstone";
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
