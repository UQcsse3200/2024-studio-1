package com.csse3200.game.components.player.inventory;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import java.util.Random;


public class Tombstone extends BuffItem {
    Random random;

    /**
     * Returns the string of the buff item
     *
     * @return string of the item
     */
    @Override
    public String getBuffSpecification() {
        return "tombstone";
    }

    /**
     * get the amount the damage is buffed by.
     * @return the amount the damages is buff by.
     */
//    public int getBuff() {
//        return buff;
//    }

    /**
     * Applies the damage buff to the player for each weapon
     *
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {

        //GridPoint2 itemEntityPosition = new GridPoint2(xPosition, yPosition);
        //markEntityForRemoval(collisionItemEntity);

        int randomInt = this.random.nextInt(2);
        Entity newItem = this.randomPetGenerator(randomInt);
        //ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(newItem, itemEntityPosition, true, true);
        //entity.getEvents().trigger("updateItemsReroll", newItem, collisionItemEntity);


    }
    private Entity randomPetGenerator(int randomNum) {
        String specification = null;
        switch (randomNum) {
            case 0 -> specification = "bat";
            case 1 -> specification = "bear";
//                case 2 -> specification = "item:medkit";
//                case 3 -> specification = "buff:energydrink:High";
//                case 4 -> specification = "buff:energydrink:Low";
//                case 5 -> specification = "buff:energydrink:Medium";
        }
        //USE SPAWNENTITYAT() TO SPAWN EACH ANIMAL
        return collectibleFactory.createCollectibleEntity(specification);
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

    /**
     * Removes the item from inventory
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {

    }

}
