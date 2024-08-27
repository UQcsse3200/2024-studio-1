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
    private static List<List<String>> animals = Arrays.asList(Arrays.asList("Ghost","Ghost","Ghost"),Arrays.asList("Ghost","GhostKing","GhostKing"));
    private static GameArea gameArea;

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

        Entity spawn = new Entity();
        //spawn the specific animal
        switch (animal) {
            case "Ghost":
                spawn = NPCFactory.createGhost(player);
                break;
            case "GhostKing":
                spawn = NPCFactory.createGhostKing(player);
                break;
            //animals that won't work until merge
            /*
            case "Rat":
                spawn = NPCFactory.createRat(player);
                break;
            case "Bear":
                spawn = NPCFactory.createBear(player);
                break;
            case "Snake":
                spawn = NPCFactory.createSnake(player);
                break;
            case "Dino":
                spawn = NPCFactory.createDino(player);
                break;
            case "Bat":
                spawn = NPCFactory.createBat(player);
                break;
            case "Dog":
                spawn = NPCFactory.createDog(player);
                break;
            case "Croc":
                spawn = NPCFactory.createCroc(player);
                break;
            case "Gorilla":
                spawn = NPCFactory.createGorilla(player);
                break;
             */
            default:
                System.out.println("Unknown animal: " + animal);
                break;
        }
        gameArea.spawnEntityAt(spawn, pos, true, true);
    }
}