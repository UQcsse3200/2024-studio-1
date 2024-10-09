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

    public void spawnRandomEnemies(String enemy) {
        Entity newEnemy = npcFactory.create(enemy, player);
        GridPoint2 pos = getRandomPosition();
        spawnEnemyEntity(area, newEnemy, pos);
    }

    private GridPoint2 getRandomPosition() {
        int x = random.nextInt(maxGridPoint.x - minGridPoint.x + 1) + minGridPoint.x;
        int y = random.nextInt(maxGridPoint.y - minGridPoint.y + 1) + minGridPoint.y;
        return new GridPoint2(x, y);
    }
}