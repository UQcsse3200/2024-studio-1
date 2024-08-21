package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.DoorConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class DoorFactory {
    public static Entity createDoor() {
        // Room variables will be stored once map rooms instantiated
        Entity door = createBaseDoor();


        // door config can be implemented later if deigned necessary

        door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        door.getComponent(TextureRenderComponent.class).scaleEntity();
        door.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(door, 0.5f, 0.2f);
        return door;
    }

    public static Entity createBaseDoor() {
        Entity door =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/items/rounded_door.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        return door;
    }
}

