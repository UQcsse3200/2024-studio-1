package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;

/**
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant shotgun animation based on the current event.
 */
public class WeaponAnimationController extends Component {
    WeaponAnimationRenderComponent animationController;

    InventoryComponent inventoryComponent;

    @Override
    public void create() {
        super.create();
        animationController = this.entity.getComponent(WeaponAnimationRenderComponent.class);
    }

    public void updateHost(Entity hostEntity) {
        hostEntity.getEvents().addListener("walkLeft", this::animateLeft);
        hostEntity.getEvents().addListener("walkRight", this::animateIdle);
    }

    private void animateLeft() {
        animationController.startAnimation("left");
    }

    private void animateIdle() {
        animationController.startAnimation("idle");
    }
}
