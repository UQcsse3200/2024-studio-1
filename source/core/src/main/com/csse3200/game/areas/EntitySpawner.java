package com.csse3200.game.areas;

import java.util.List;
import java.util.Arrays;
import java.lang.Math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.utils.math.RandomUtils;

public class AnimalSpawner {
    //private static List<String> animals = Arrays.asList("Ghost","GhostKing");
    private static List<List<String>> animals = Arrays.asList(Arrays.asList("Rat","Dog","Minotaur"),Arrays.asList("Dino","Bat","Bear"));
    private static GameArea gameArea;
    private static final NPCFactory npcFactory = new NPCFactory();

    public static void setGameArea(GameArea ga) { gameArea = ga;}

    public static void addAnimalGroups(List<String> newGroup) { animals.add(newGroup); }

    public static void spawnAnimalGroup(Entity player, int index,GridPoint2 minPos,GridPoint2 maxPos)
    {
        //check if index is valid
        if (index < 0 || index >= animals.size()) {
            System.out.println("Invalid index for animal group");
            return;
        }
        for (String animal : animals.get(index)) {
            //get a random position
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Vector2 playerPosition = player.getPosition();
            while(Math.abs(playerPosition.x - randomPos.x) <= 1 && Math.abs(playerPosition.y - randomPos.y) <= 1 ) {
                randomPos = RandomUtils.random(minPos, maxPos);
            }
            spawnAnimal(player, animal,randomPos);
        }
    }

    private static void spawnAnimal(Entity player,String animal, GridPoint2 pos) {

        Entity spawn = switch (animal) {
            case "Rat" -> npcFactory.createRat(player);
            case "Bear" -> npcFactory.createBear(player);
            case "Snake" -> npcFactory.createSnake(player);
            case "Dino" -> npcFactory.createDino(player);
            case "Bat" -> npcFactory.createBat(player);
            case "Dog" -> npcFactory.createDog(player);
            case "Minotaur" -> npcFactory.createMinotaur(player);
            default -> throw new IllegalArgumentException("Unknown animal: " + animal);
        };
        gameArea.spawnEntityAt(spawn, pos, true, true);
    }
}