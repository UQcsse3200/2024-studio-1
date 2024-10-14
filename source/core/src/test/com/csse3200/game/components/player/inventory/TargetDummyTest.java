package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.player.inventory.usables.TargetDummy;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class TargetDummyTest {
    private Entity playerEntity;
    private Entity targetDummy;
    private TargetDummy targetDummyItem;
    private GameAreaService gameAreaService;
    private GameController gameController;
    private EnemyRoom enemyRoom;
    private ResourceService resourceService;
    private AITaskComponent aiTaskComponent;

    @BeforeEach
    public void setUp() {
        // Mock entities, components, and services
        playerEntity = mock(Entity.class);
        targetDummy = mock(Entity.class);
        gameAreaService = mock(GameAreaService.class);
        gameController = mock(GameController.class);
        enemyRoom = mock(EnemyRoom.class);
        aiTaskComponent = mock(AITaskComponent.class);
        resourceService = mock(ResourceService.class); // Mock ResourceService

        // Register the mocked ResourceService with ServiceLocator before creating the factory
        ServiceLocator.registerResourceService(resourceService);

        // Register other services
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock texture loading
        Texture dummyTexture = mock(Texture.class);
        when(resourceService.getAsset("images/items/target_dummy_deployed.png", Texture.class)).thenReturn(dummyTexture);

        // Mock player position
        Vector2 playerPosition = new Vector2(10, 10);
        when(playerEntity.getPosition()).thenReturn(playerPosition);

        // Mock GameAreaService
        when(gameAreaService.getGameController()).thenReturn(gameController);

    }

    @Test
    public void testGetName() {
        targetDummyItem = new TargetDummy();
        assertEquals("Target Dummy", targetDummyItem.getName());
    }

    @Test
    public void testGetItemSpecification() {
        targetDummyItem = new TargetDummy();
        assertEquals("targetdummy", targetDummyItem.getItemSpecification());
    }


//    @Test
//    public void testSpawnTargetDummy() {
        // Initialize TargetDummy after ResourceService has been registered
//        TargetDummy targetDummyItem = new TargetDummy();

        // Call apply method which internally calls spawnTargetDummy
//        targetDummyItem.apply(playerEntity);

        // Verify that the target dummy was spawned at the correct position
//        verify(mainGameArea).spawnEntityAt(any(Entity.class), eq(new GridPoint2(10, 10)), eq(true), eq(true));
//    }

//    @Test
//    public void testEnemyTargetsUpdated() {
        // Mock enemy room and enemies
//        Entity enemy1 = mock(Entity.class);
//        Entity enemy2 = mock(Entity.class);
//        List<Entity> enemies = Arrays.asList(enemy1, enemy2);
//        when(enemyRoom.getEnemies()).thenReturn(enemies);
//        when(mainGameArea.getCurrentRoom()).thenReturn(enemyRoom);
//    }


}
