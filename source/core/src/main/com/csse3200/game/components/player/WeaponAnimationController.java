package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.InventoryComponent;
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
        inventoryComponent = this.entity.getComponent(InventoryComponent.class);
        animationController = this.entity.getComponent(WeaponAnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft", this::animateLeft);
        entity.getEvents().addListener("walkRight", this::animateIdle);
    }

    private void animateLeft() {
        if(inventoryComponent.getRangedWeaponCount() > 0){
        animationController.startAnimation("left");}
    }

    private void animateIdle() {
        if(inventoryComponent.getRangedWeaponCount() > 0){
        animationController.startAnimation("idle");}
    }
}
