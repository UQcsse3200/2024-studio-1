package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;

import java.util.Arrays;
import java.util.List;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.utils.math.RandomUtils;

/**
 * A boos room of the game,
 * these often have unique animals and rewards.
 */
public class BossRoom extends BaseRoom {
    private final String specification;
    private final static List<List<String>> ANIMAL_SPECIFICATIONS = List.of(
            //Currently there are three random animals being spawned in base on the level the player is in. Bosses haven't been implemented thus using
            //currently available animals.
            List.of("Rat"),//change to boss 1
            List.of("Minotaur"),//boss 2
            List.of("Bear"),// boss 4
            List.of("Werewolf")//boss 3
    );

    private final static List<List<String>> ITEM_SPECIFICATIONS = List.of(
            //List of three lists of items for 3 different levels to be spawned in base on which level player is in.
            List.of("buff:energydrink", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink")
    );

    private static final float WALL_THICKNESS = 0.15f;

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
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections);
        this.specification = specification;
    }

    @Override
    public void spawn(Entity player, MainGameArea mainGameArea) {
        this.spawnTerrain(mainGameArea, WALL_THICKNESS);
        List<String> split = Arrays.stream(specification.split(",")).toList();
        GridPoint2 min = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        GridPoint2 max = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );
        //if level == 1 then index 1
        // for boss room the specification should be different
        int animalGroup = Integer.parseInt(split.get(4));
        for (String s : ANIMAL_SPECIFICATIONS.get(animalGroup)) {
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnAnimal(mainGameArea, player, s, randomPos);
        }

        int itemGroup = Integer.parseInt(split.get(5));
        for (String s : ITEM_SPECIFICATIONS.get(itemGroup)) {
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnItem(mainGameArea, s, randomPos);
        }
    }
}
