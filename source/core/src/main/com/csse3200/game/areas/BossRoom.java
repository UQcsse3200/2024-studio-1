package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.StairFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Represents a boss room in the game.
 * Boss rooms often have unique animals and rewards.
 * This class extends EnemyRoom to include boss-specific functionality.
 */
public class BossRoom extends EnemyRoom {
    private NPCFactory npcFactory;
    private GameArea area;
    private Entity player;
    private Entity stairs;

    /**
     * Retrieves the specifications for animals in the boss room.
     * @return A list of lists containing animal specifications for different boss levels.
     */
    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                List.of("Birdman"),  // boss 1
                List.of("Kitsune"),   // boss 3
                List.of("Cthulu")  // boss 3
        );
    }

    /**
     * Retrieves the specifications for items in the boss room.
     * @return A list of lists containing item specifications for different boss levels.
     */
    @Override
    protected List<List<String>> getItemSpecifications() {
        return List.of(
                //List of three lists of items for 3 different levels to be spawned in base on which level player is in.
                List.of("buff:energydrink:High", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
                List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink:High", "item:shieldpotion"),
                List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink:High")
        );
    }

    /**
     * Constructs a BossRoom with specific features.
     *
     * @param npcFactory         The NPC factory to use for creating NPCs.
     * @param collectibleFactory The Collectible factory to use for creating collectible items.
     * @param terrainFactory     The terrain factory to use for creating terrain.
     * @param roomConnections    The keys for all the adjacent rooms.
     * @param specification      The specification for this room.
     * @param roomName           The name of the room.
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

    /**
     * Spawns entities in the boss room, including the player and stairs.
     *
     * @param player The player entity to spawn.
     * @param area   The main game area where spawning occurs.
     */
    @Override
    public void spawn(Entity player, GameArea area) {
        super.spawn(player, area);
        this.area = area;
        this.player = player;
        spawnStairs(player, area);

    }

    private void spawnStairs(Entity player, GameArea area) {
        if (stairs == null) {
            stairs = StairFactory.createStair(player.getId());
            int x = maxGridPoint.x;
            int y = maxGridPoint.y;
            GridPoint2 pos = new GridPoint2(x, y);
            area.spawnEntityAt(stairs, pos, true, true);
        }
    }

    /**
     * Spawns random enemies in the boss room.
     *
     * @param enemy The type of enemy to spawn.
     */
    public void spawnRandomEnemies(String enemy) {
        Entity newEnemy = npcFactory.create(enemy, player);
        GridPoint2 pos = getRandomPosition();
        spawnEnemyEntity(area, newEnemy, pos);
    }

    /**
     * Generates a random position within the room's boundaries.
     *
     * @return A GridPoint2 representing a random position in the room.
     */
    private GridPoint2 getRandomPosition() {
        int x = random.nextInt(maxGridPoint.x - minGridPoint.x + 1) + minGridPoint.x;
        int y = random.nextInt(maxGridPoint.y - minGridPoint.y + 1) + minGridPoint.y;
        return new GridPoint2(x, y);
    }



    @Override
    public void removeRoom() {
        super.removeRoom();
        if (stairs != null) {
            ServiceLocator.getEntityService().markEntityForRemoval(stairs);
            stairs = null;
        }
    }
}
