package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MainRoom extends BaseRoom {
    private static final Logger logger = LoggerFactory.getLogger(MainRoom.class);
    private final String specification;
    List<List<String>> animalSpecifications = List.of(
            List.of("ProjectileRat", "Rat", "Dog", "Minotaur", "Dino", "Bear", "Snake", "Bat"),
            List.of("Bear", "Snake", "Dino"),
            List.of("Bear", "Bear", "Minotaur"),
            List.of("Snake", "Bat", "Minotaur"),
            List.of("Bat", "Bat", "Bat"),
            List.of("Minotaur", "Minotaur", "Minotaur"),
            List.of("Rat", "Bat", "Bear")
    );
    List<List<String>> itemSpecifications = List.of(
            List.of("buff:energydrink", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"),
            List.of("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink", "item:shieldpotion"),
            List.of("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink"),
            List.of("item:shieldpotion", "ranged:shotgun", "melee:knife", "item:medkit", "buff:energydrink"),
            List.of("melee:knife", "item:bandage", "ranged:shotgun", "item:shieldpotion", "item:medkit"),
            List.of("buff:energydrink", "item:shieldpotion", "ranged:shotgun", "melee:knife", "item:bandage"),
            List.of("item:medkit", "melee:knife", "buff:energydrink", "ranged:shotgun", "item:shieldpotion")
    );

    private static final float WALL_THICKNESS = 0.15f;

    public MainRoom(NPCFactory npcFactory,
                    CollectibleFactory collectibleFactory,
                    TerrainFactory terrainFactory,
                    List<String> roomConnections,
                    String specification) {
        super(npcFactory, collectibleFactory, terrainFactory, roomConnections);
        this.specification = specification;
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        logger.info("Spawning: {}", specification);

        logger.info("Spawning terrain:");
        this.spawnTerrain(area, WALL_THICKNESS);
        logger.info("Spawning doors:");
        this.spawnDoors(area, player);
        List<String> split = Arrays.stream(specification.split(",")).toList();
        GridPoint2 min = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        GridPoint2 max = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );
        logger.info("Spawning animals:");
        int animalGroup = Integer.parseInt(split.get(4));
        for (String s : animalSpecifications.get(animalGroup)){
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnAnimal(area, player, s, randomPos);
        }

        logger.info("Spawning items:");
        int itemGroup = Integer.parseInt(split.get(5));
        for (String s : itemSpecifications.get(itemGroup)){
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnItem(area, s, randomPos);
        }
    }
}
