package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.DoorConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import static java.lang.String.format;

public class DoorFactory {
    static Entity door;
    public static Entity createDoor(char orientation, DoorCallBack callback) {
        // Room variables will be stored once map rooms instantiated
        door = createBaseDoor(orientation);
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
        PhysicsUtils.setScaledCollider(door, 0.18f, 0.8f); // 0.5f, 0.2f
        createCollision(callback);

        return door;
    }

    private static void createCollision(DoorCallBack callback) {
        door.getEvents().addListener("collisionStart", (Fixture other, Fixture other1) -> {
            if (callback != null) {
                callback.onDoorCollided();
            }
        });
    }

    public static Entity createBaseDoor(char orientation) {
        Entity door =
                new Entity()
                        .addComponent(new TextureRenderComponent(format("images/rounded_door_%c.png",orientation)))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        return door;
    }
}

