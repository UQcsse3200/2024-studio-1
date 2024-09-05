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
    int playerId;

    /**
     * Create door with specific orientation and callback function implementation
     *
     * @param orientation: char - sets orientation for door when returned
     * @param idOfPlayer: int - idOfPlayer to confirm interaction is with player
     * @param next_Room: string - string of next room to jump to
     */
    public Door(char orientation, int idOfPlayer, String next_Room) {
        super();
        playerId = idOfPlayer;

        this.addComponent(new TextureRenderComponent(format("images/rounded_door_%c.png",orientation)))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        // door config can be implemented later if deigned necessary
        this.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        this.getComponent(TextureRenderComponent.class).scaleEntity();
        if(orientation == 'v'){
            this.scaleHeight(1f); //2.5f
            PhysicsUtils.setScaledCollider(this, 0.18f, 0.8f);
        }else {
            this.scaleWidth(1f);
            PhysicsUtils.setScaledCollider(this, 0.8f, 0.18f);
        }
        // Change orientation of physics collider to fit door texture orientation

        PhysicsUtils.setScaledCollider(this, 0.18f, 0.8f); // 0.5f, 0.2f

        createCollision(next_Room);
    }

    /**
     * Create collision listener to change rooms
     *
     * @param Room : string representation of the room jumping to
     */
    private void createCollision(String Room) {
        
        this.getEvents().addListener("collisionStart", (Fixture fixture1, Fixture fixture2) -> {
            Entity entity2 = (Entity) fixture2.getUserData();
            if (entity2.getId() == playerId) {
                
                System.out.println("this is the room " + Room);
                
                ServiceLocator.getGameAreaService().getGameArea().changeRooms(Room);
            }
        });
    }
}
