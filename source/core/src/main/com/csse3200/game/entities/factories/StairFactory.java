package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

import com.csse3200.game.services.ServiceLocator;

/**
 * Create stair with necessary components
 */
public class StairFactory {
    /**
     * Door entity to track updates to entity over different function calls
     */
    static Entity stair;
    static int playerId;

    /**
     * Create stair
     * @param idOfPlayer player id
     * @return stair: Entity
     */
    public static Entity createStair(int idOfPlayer) {
        stair = createBaseStair();
        playerId = idOfPlayer;
        createCollision();
        return stair;
    }

    /**
     * Add listener to stair entity when collision starts for callback onStairCollide function
     */
    private static void createCollision() {
        stair.getEvents().addListener("collisionStart", (Fixture fixture1, Fixture fixture2) -> {
            Entity entity2 = (Entity) fixture2.getUserData();
            if (entity2.getId() == playerId) {
                if(ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom().getIsRoomComplete()) {
                    moveToNextLevel();
                }
            }
        });
    }

    /**
     * Create base entity with necessary components
     * @return door: Entity - base stair
     */
    public static Entity createBaseStair() {
        return new Entity()
                .addComponent(new NameComponent("stairs to next level"))
                .addComponent(new TextureRenderComponent("images/staircase.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    }

    /**
     * checks if the player wants to move to next level
     */
   /* private static void alertGoToNextLevel(int currentLevel) {
        ServiceLocator.getAlertBoxService().showConfirmationDialog("Confirm", "Do you want to proceed to the next level?", new AlertBoxService.ConfirmationListener() {
            @Override
            public void onYes() {
                // TODO: Move to next level
                ServiceLocator.getEntityService().markEntityForRemoval(stair);
                ServiceLocator.getGameAreaService().getGameArea().changeLevel(currentLevel+1);
            }

            @Override
            public void onNo() {
                // TODO: Restart level
                ServiceLocator.getGameAreaService().getGameArea().changeLevel(currentLevel);
            }
        });
    }*/

    /**
     * Moves the player to the next level
     */

    private static void moveToNextLevel() {
        int currentLevel = ServiceLocator.getGameAreaService().getGameArea().getCurrentLevel().getLevelNumber();
        ServiceLocator.getEntityService().markEntityForRemoval(stair);
        ServiceLocator.getGameAreaService().getGameArea().changeLevel(currentLevel + 1);
    }
}
