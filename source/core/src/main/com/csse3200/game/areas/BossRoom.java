package com.csse3200.game.areas;

import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.Door;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.StairFactory;
import com.sun.tools.javac.Main;


/**
 * A boos room of the game,
 * these often have unique animals and rewards.
 */
public class BossRoom extends BaseRoom {

    private static final float WALL_THICKNESS = 0.15f;
    private Entity stairs;
    @Override
    protected void initializeSpecifications() {
        this.animalSpecifications = List.of(
            //Currently there are three random animals being spawned in base on the level the player is in. Bosses haven't been implemented thus using
            //currently available animals.
            List.of("Rat"),//change to boss 1
            List.of("Minotaur"),//boss 2
            List.of("Bear"),// boss 4
            List.of("Werewolf")//boss 3
    );


    this.itemSpecifications = List.of(
            //List of three lists of items for 3 different levels to be spawned in base on which level player is in.
            List.of("buff:energydrink:High", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink:High", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink:High")
    );
    
    this.isBossRoom = true;
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
                    String specification) {
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections, specification);
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);
        spawnStairs(player, area);
    }

    private void spawnStairs (Entity player, MainGameArea area) {
        stairs = StairFactory.createStair(player.getId());
        int x = maxGridPoint.x/2;
        int y = maxGridPoint.y;
        GridPoint2 pos = new GridPoint2(x,y);
        area.spawnEntityAt(stairs, pos, true ,true);
    }


}
