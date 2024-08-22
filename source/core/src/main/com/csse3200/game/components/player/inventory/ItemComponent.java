package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;

public class ItemComponent extends Component {
    private final Collectible item;

    /**
     * Construct a new Component that holds a collectible.
     *
     * @param item the collectible that this component holds.
     */
    public ItemComponent (Collectible item) {
        this.item = item;
    }

    /**
     * Called when the entity is created and registered.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

  /*
  This method was mainly based off of the TouchPlayerInputComponent.java class
   */
    private void onCollisionStart(Fixture me, Fixture other) {
        //Make the inventory 'pickup' this item upon collision
        //Gets the Inventory component from this entity, and uses the 'pickup' method from InventoryComponent.java
        //In InventoryComponent.pickup(item), it calls upon the pickup method of that item
        //The EnergyDrink should apply an effect as soon as it is picked up
        entity.getComponent(InventoryComponent.class).pickup(item);
        System.out.println("Is touching");
    }
}
