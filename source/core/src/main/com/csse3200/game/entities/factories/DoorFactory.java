package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * Create door with necessary components
 */
public class DoorFactory {
    private static final Logger log = LoggerFactory.getLogger(DoorFactory.class);

    /**
     * Create door with specific orientation and callback function implementation
     *
     * @param orientation: char - sets orientation for door when returned
     * @param idOfPlayer:  int - idOfPlayer to confirm interaction is with player
     * @param next_Room:   string - string of next room to jump to
     */
    public Entity create(char orientation, int idOfPlayer, String next_Room) {
        log.info("Creating Door {}, {}", orientation, next_Room);

        Entity door = new Entity();
        door.addComponent(new NameComponent("Door to \"" + next_Room + "\""));
        door.addComponent(new TextureRenderComponent(format("images/rounded_door_%c.png", orientation)));
        PhysicsComponent physicsComponent = new PhysicsComponent();
        door.addComponent(physicsComponent);
        door.addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        // door config can be implemented later if deigned necessary
        door.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        door.getComponent(TextureRenderComponent.class).scaleEntity();
        if (orientation == 'v') {
            door.scaleHeight(1f); //2.5f
            PhysicsUtils.setScaledCollider(door, 0.18f, 0.8f);
        } else {
            door.scaleWidth(1f);
            PhysicsUtils.setScaledCollider(door, 0.8f, 0.18f);
        }
        // Change orientation of physics collider to fit door texture orientation

        PhysicsUtils.setScaledCollider(door, 0.18f, 0.8f); // 0.5f, 0.2f

        createCollision(door, next_Room, idOfPlayer);

        return door;
    }

    /**
     * Create collision listener to change rooms
     *
     * @param room : string representation of the room jumping to
     */
    private void createCollision(Entity door, String room, int playerId) {
        door.getEvents().addListener("collisionStart", (Fixture fixture1, Fixture fixture2) -> {
            if (fixture2.getUserData() instanceof Entity entity2 &&
                    entity2.getId() == playerId) {
                log.info("this is the room {}", room);
                if (ServiceLocator.getGameAreaService().getGameController().getCurrentRoom().getIsRoomComplete()) {
                    ServiceLocator.getGameAreaService().getGameController().changeRooms(room);
                }
            }
        });
    }
}
