package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.DoorConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class DoorFactory {

    public static createBaseDoor() {
        Entity door =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComonent)
    }
}

