package com.csse3200.game.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;

import java.util.Objects;

/**
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant shotgun animation based on the current event.
 */
public class WeaponAnimationController extends Component {
    WeaponAnimationRenderComponent animationController;

    Vector2 weaponDirection;
    Vector2 up;
    Vector2 down;
    Vector2 left;
    Vector2 right;
    InventoryComponent inventoryComponent;

    @Override
    public void create() {
        super.create();
        animationController = this.entity.getComponent(WeaponAnimationRenderComponent.class);
        up = new Vector2(0.0f, 1.0f);
        down = new Vector2(0.0f, -1.0f);
        left = new Vector2(1.0f, 0.0f);
        right = new Vector2(-1.0f, 0.0f);
    }

    public void connectPlayer(Entity player) {
        if (Objects.equals(this.getEntity().getComponent(NameComponent.class).getName(),
                "Ranged")) {
            player.getEvents().addListener("walkLeft", this::animateLeft);
            player.getEvents().addListener("walkRight", this::animateIdle);
            player.getEvents().addListener("walkUp", this::animateUp);
            player.getEvents().addListener("walkDown", this::animateDown);
        }
        this.getEntity().getEvents().addListener("shootRight", this::animateShootRight);
        this.getEntity().getEvents().addListener("shootLeft", this::animateShootLeft);
        this.getEntity().getEvents().addListener("shootUp", this::animateShootUp);
        this.getEntity().getEvents().addListener("shootDown", this::animateShootDown);
    }

    private void animateLeft() {
        animationController.startAnimation("left");
        weaponDirection = left;
    }

    private void animateIdle() {
        animationController.startAnimation("idle");
        weaponDirection = right;
    }

    private void animateUp() {
        animationController.startAnimation("up");
        weaponDirection = up;
    }
    private void animateDown() {
        animationController.startAnimation("down");
        weaponDirection = down;
    }
    private void animateShootRight() {
        animationController.startAnimation("shootRight");
    }
    private void animateShootLeft() {
        animationController.startAnimation("shootLeft");
    }
    private void animateShootUp() {
        animationController.startAnimation("shootUp");
    }
    private void animateShootDown() {
        animationController.startAnimation("shootDown");
    }
}
