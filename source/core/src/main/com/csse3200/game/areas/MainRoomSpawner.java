package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.Arrays;
import java.util.List;

public class MainRoomSpawner extends BaseRoomSpawner {
    List<List<String>> animalSpecifications = List.of(
            List.of("Rat", "Dog", "Minotaur", "Dino", "Bear", "Snake", "Bat"),
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

    public MainRoomSpawner(GameArea gameArea, NPCFactory npcFactory, CollectibleFactory collectibleFactory,  TerrainFactory terrainFactory) {
        super(gameArea, npcFactory, collectibleFactory, terrainFactory);
    }

    @Override
    public void spawnRoom(Entity player, String specification) {
        this.spawnTerrain(WALL_THICKNESS);
        List<String> split = Arrays.stream(specification.split(",")).toList();
        GridPoint2 min = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        GridPoint2 max = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );
        int animalGroup = Integer.parseInt(split.get(4));
        for (String s : animalSpecifications.get(animalGroup)){
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnAnimal(player, s, randomPos);
        }

        int itemGroup = Integer.parseInt(split.get(5));
        for (String s : itemSpecifications.get(itemGroup)){
            GridPoint2 randomPos = RandomUtils.random(min, max);
            this.spawnItem(s, randomPos);
        }
    }
}
