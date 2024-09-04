package com.csse3200.game.areas;

import java.util.List;
import java.util.Arrays;
import java.lang.Math;
import java.util.ArrayList;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.utils.RandomNumberGenerator;


public class EntitySpawner {
    private static RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator("HiCameron");
    private static GameArea gameArea;

    private static List<List<String>> animals = new ArrayList<>();

    private static List<List<String>> items = new ArrayList<>();

    private static final NPCFactory npcFactory = new NPCFactory();

    private static final ProjectileFactory projectileFactory = new ProjectileFactory();

    public static void setGameArea(GameArea ga) { gameArea = ga;}

    public static void addAnimalGroups(List<String> newGroup) { animals.add(newGroup); }
    public static void addItemGroups(List<String> newGroup) { items.add(newGroup); }

    public static void spawnAnimalGroup(Entity player, int index,GridPoint2 minPos,GridPoint2 maxPos)
    {
        //check if index is valid
        if (index < 0 || index >= animals.size()) {
            throw new IllegalArgumentException("Invalid index for animal group");
        }
        for (String animal : animals.get(index)) {
            boolean willSpawn = true;//*dont want to spawn nothing in worst case scenario -> to do next sprint
            // (randomNumberGenerator.getRandomInt(0,1000) % 2 == 1);
            if(willSpawn) {
                //get a random position
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                Vector2 playerPosition = player.getPosition();
                while (Math.abs(playerPosition.x - randomPos.x) <= 1 && Math.abs(playerPosition.y - randomPos.y) <= 1) {
                    randomPos = RandomUtils.random(minPos, maxPos);
                }
                spawnAnimal(player, animal, randomPos);
            }
        }
    }

    public static void spawnItemGroup(int index,GridPoint2 minPos,GridPoint2 maxPos){
        //check if index is valid
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Invalid index for item group");
        }
        for (String item : items.get(index)) {
            //get a random position
            boolean willSpawn = (randomNumberGenerator.getRandomInt(0,1000) % 2 == 1);
            if(willSpawn) {
                GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
                spawnItem(item, randomPos);
            }
        }
    }

    private static void spawnItem(String itemName,GridPoint2 pos){
        Entity item = CollectibleFactory.createCollectibleEntity(itemName);
        gameArea.spawnEntityAt(item,pos,true,true);
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