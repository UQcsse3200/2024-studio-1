package com.csse3200.game.areas;

import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.StairFactory;


/**
 * A boss room of the game,
 * these often have unique animals and rewards.
 */
public class BossRoom extends EnemyRoom {

    @Override
    protected List<List<String>> getAnimalSpecifications() {
        return List.of(
                //Currently there are three random animals being spawned in base on the level the player is in. Bosses haven't been implemented thus using
                //currently available animals.
                List.of("Werewolf"),//boss 1
                List.of("Birdman"),//boss 2
                List.of("Kitsune")// boss 3
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
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        spawnStairs(player, area);
    }

    private void spawnStairs(Entity player, MainGameArea area) {
        Entity stairs = StairFactory.createStair(player.getId());
        int x = maxGridPoint.x;
        int y = maxGridPoint.y;
        GridPoint2 pos = new GridPoint2(x, y);
        area.spawnEntityAt(stairs, pos, true, true);
    }


}
