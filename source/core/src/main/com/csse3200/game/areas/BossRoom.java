package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.StairFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * A boss room of the game,
 * these often have unique animals and rewards.
 */
public class BossRoom extends EnemyRoom {
    private final NPCFactory npcFactory;
    private MainGameArea area;
    private Entity player;

    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                List.of("Werewolf"), // boss 1
                List.of("Kitsune"), // boss 2
                List.of("Birdman")  // boss 3
        );
    }

    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                //List of three lists of items for 3 different levels to be spawned in base on which level player is in.
                List.of("buff:energydrink:High", "item:medkit", "melee:Knife", "ranged:Shotgun", "item:shieldpotion"),
                List.of("item:bandage", "melee:Knife", "ranged:Shotgun", "buff:energydrink:High", "item:shieldpotion"),
                List.of("ranged:Shotgun", "item:medkit", "melee:Knife", "item:bandage", "buff:energydrink:High")
        );
    }

    /**
     * A boss room with particular features.
     *
     * @param npcFactory         the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory     the terrain factory to use.
     * @param roomConnections    the keys for all the adjacent rooms.
     * @param specification      the specification for this room.
     */
    public BossRoom(NPCFactory npcFactory,
                    CollectibleFactory collectibleFactory,
                    TerrainFactory terrainFactory,
                    List<String> roomConnections,
                    String specification,
                    String roomName) {
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections, specification, roomName);
        this.npcFactory = npcFactory;
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        this.area = area;
        this.player = player;
        spawnStairs(player, area);
    }

    private void spawnStairs(Entity player, MainGameArea area) {
        Entity stairs = StairFactory.createStair(player.getId());
        int x = maxGridPoint.x;
        int y = maxGridPoint.y;
        GridPoint2 pos = new GridPoint2(x, y);
        area.spawnEntityAt(stairs, pos, true, true);
    }

    /**
     * Spawns a dog and a snake near the boss when health reaches 50%.
     *
     */
    public void spawnOtherAnimals() {
        // Create dog and snake entities using NPCFactory
        Entity dog = npcFactory.create("Dog", player);
        Entity snake = npcFactory.create("Snake", player);

        // Fixed positions to spawn the dog and snake
        GridPoint2 dogPos = new GridPoint2(5, 5);  // Example fixed position
        GridPoint2 snakePos = new GridPoint2(10, 10);  // Example fixed position

        // Spawn the animals at the fixed positions
        BossRoom bossRoom = (BossRoom) ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom();

        bossRoom.spawnEnemyEntity(area, dog, dogPos);
        bossRoom.spawnEnemyEntity(area, snake, snakePos);
//        area.spawnEntityAt(snake, snakePos, true, true);
    }
}
