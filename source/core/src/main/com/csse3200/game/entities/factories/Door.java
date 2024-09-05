package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import static java.lang.String.format;

/**
 * Create door with necessary components
 */
public class Door extends Entity {
    /**
     * Door entity to track updates to entity over different function calls
     */
    Entity door;
    int playerId;

    /**
     * Create door with specific orientation and callback function implementation
     *
     * @param orientation: char - sets orientation for door when returned
     * @param callback: DoorCallBack - call callback onDoorCollided function when event collision is triggered
     */
    public Door(char orientation, int idOfPlayer, String next_Room) {
        super();
        door = createBaseDoor(orientation);
        playerId = idOfPlayer;
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

        createCollision(next_Room);
    }

    /**
     * Add listener to door entity when collision starts for callback onDoorCollide function
     *
     * @param callback: DoorCallBack - callback onDoorCollide to be set within door when door collided with
     */
    private void createCollision(String Room) {
        
        door.getEvents().addListener("collisionStart", (Fixture fixture1, Fixture fixture2) -> {
            Entity entity2 = (Entity) fixture2.getUserData();
            if (entity2.getId() == playerId) {
                
                System.out.println("this is the room " + Room);
                
                ServiceLocator.getGameAreaService().getGameArea().changeRooms(Room);
            }
        });
    }

    /**
     * Create base entity with necessary components
     * @param orientation: char - orientation of texture rendered
     * @return door: Entity - base door
     */
    private Entity createBaseDoor(char orientation) {
        Entity door =
                new Entity()
                        .addComponent(new TextureRenderComponent(format("images/rounded_door_%c.png",orientation)))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        return door;
    }
}
