package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import static java.lang.String.format;

/**
 * Create door with necessary components
 */
public class DoorFactory extends LoadedFactory {
    /**
     * Create door with specific orientation and callback function implementation
     *
     * @param orientation: char - sets orientation for door when returned
     * @param callback: DoorCallBack - call callback onDoorCollided function when event collision is triggered
     * @return door: Entity
     */
    public Entity createDoor(Entity player, char orientation, DoorCallBack callback) {
        Entity door = createBaseDoor(orientation);
        // door config can be implemented later if deigned necessary
        door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        door.getComponent(TextureRenderComponent.class).scaleEntity();
        if(orientation == 'v'){
            door.scaleHeight(1f); //2.5f
            PhysicsUtils.setScaledCollider(door, 0.18f, 0.8f);
        }else {
            door.scaleWidth(1f);
            PhysicsUtils.setScaledCollider(door, 0.8f, 0.18f);
        }
        // Change orientation of physics collider to fit door texture orientation

        PhysicsUtils.setScaledCollider(door, 0.18f, 0.8f); // 0.5f, 0.2f
        createCollision(door, callback, player);

        return door;
    }

    /**
     * Add listener to door entity when collision starts for callback onDoorCollide function
     *
     * @param callback: DoorCallBack - callback onDoorCollide to be set within door when door collided with
     */
    private void createCollision(Entity door, DoorCallBack callback, Entity player) {
        door.getEvents().addListener("collisionStart", (Fixture fixture1, Fixture fixture2) -> {
            Entity entity = (Entity) fixture2.getUserData();
            if (callback != null && entity.equals(player)) {
                System.out.println("This worked!");
                callback.onDoorCollided();
            }
        });
    }

    /**
     * Create base entity with necessary components
     * @param orientation: char - orientation of texture rendered
     * @return door: Entity - base door
     */
    public static Entity createBaseDoor(char orientation) {
        return new Entity()
                .addComponent(new TextureRenderComponent(format("images/rounded_door_%c.png",orientation)))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/rounded_door_h.png",
                "images/rounded_door_v.png"
        };
    }
}

