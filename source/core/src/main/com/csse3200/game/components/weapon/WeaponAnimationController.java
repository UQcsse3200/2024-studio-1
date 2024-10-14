package com.csse3200.game.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant shotgun animation based on the current event.
 */
public class WeaponAnimationController extends Component {

    Logger logger = LoggerFactory.getLogger(WeaponAnimationController.class);
    WeaponAnimationRenderComponent animationController;

    Boolean connected;
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
        this.getEntity().getEvents().addListener("shootRight", this::animateShootRight);
        this.getEntity().getEvents().addListener("shootLeft", this::animateShootLeft);
        this.getEntity().getEvents().addListener("shootUp", this::animateShootUp);
        this.getEntity().getEvents().addListener("shootDown", this::animateShootDown);
    }

    /**
     * Connect this component to the player who is using the weapon to get the events from the
     * player
     * @param player the player entity
     */
    public void connectPlayer(Entity player) {
        connected = true;
        if (Objects.equals(this.getEntity().getComponent(NameComponent.class).getName(),
                "Ranged")) {
            player.getEvents().addListener("walkLeft", this::animateLeft);
            player.getEvents().addListener("walkRight", this::animateIdle);
            player.getEvents().addListener("walkUp", this::animateUp);
            player.getEvents().addListener("walkDown", this::animateDown);
        }
    }

    /**
     * Remove the player from this weapon
     * Will not play directional animation and set the animation to idle
     */
    public void disconnectPlayer() {
        this.connected = false;
        try {
            animationController.startAnimation("idle");
        } catch (Exception e) {
            logger.error("Error disconnecting player from weapon", e);
        }
    }

    private void animateLeft() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("left");
        weaponDirection = left;
    }

    private void animateIdle() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("idle");
        weaponDirection = right;
    }

    private void animateUp() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("up");
        weaponDirection = up;
    }
    private void animateDown() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("down");
        weaponDirection = down;
    }
    private void animateShootRight() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("shootRight");
    }
    private void animateShootLeft() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("shootLeft");
    }
    private void animateShootUp() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("shootUp");
    }
    private void animateShootDown() {
        if (Boolean.FALSE.equals(this.connected)) {
            return;
        }
        animationController.startAnimation("shootDown");
    }
}
