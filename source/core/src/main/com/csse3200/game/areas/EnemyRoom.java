package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EnemyRoom extends BaseRoom {
    private static final Logger logger = LoggerFactory.getLogger(EnemyRoom.class);
    protected final NPCFactory npcFactory;
    protected final int animalGroup;
    protected List<List<String>> animalSpecifications;
    protected boolean isBossRoom = false;
    private List<Entity> enemies = new ArrayList<>();

    public EnemyRoom(
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            TerrainFactory terrainFactory,
            List<String> roomConnections,
            String specification,
            String roomName) {
        super(terrainFactory, collectibleFactory, roomConnections, specification, roomName);
        this.npcFactory = npcFactory;
        this.animalSpecifications = getAnimalSpecifications();

        List<String> split = List.of(specification.split(","));
        this.animalGroup = Integer.parseInt(split.get(4));
        
    }

    public List<Entity> getEnemies() {
        return enemies;
    }


    public void checkIfRoomComplete() {
        if (isAllAnimalDead()) {
            System.out.println("room is complete");
            setIsRoomComplete();
        }
    }

    protected abstract List<List<String>> getAnimalSpecifications();

    protected void createEnemyEntities(List<String> animals, Entity player) {
        for (String animal : animals) {
            Entity enemy = npcFactory.create(animal, player);
            entities.add(enemy);
        }
    }

    public void makeAllAnimalDead() {
        for (Entity entity : enemies) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null) {
                combatStatsComponent.setHealth(1);
                combatStatsComponent.hit(combatStatsComponent);
            }
        }
    }

    public boolean isAllAnimalDead() {
        for (Entity entity : enemies) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null && !combatStatsComponent.isDead()) {
                return false;
            }
        }

        return true;
        
        
    }

    

    private void spawnItems() {
        MainGameArea area = ServiceLocator.getGameAreaService().getGameArea();
        List<String> itemGroup = this.itemSpecifications.get(this.itemGroup);
        if (itemGroup != null && itemGroup.size() >= 2) {
            spawnItem(area, itemGroup.get(0), new GridPoint2(8, 8));
            spawnItem(area, itemGroup.get(1), new GridPoint2(6, 8));
        }
    }

    protected void spawnAnimals(MainGameArea area, Entity player, GridPoint2 min, GridPoint2 max) {
        List<String> animalGroup = this.animalSpecifications.get(this.animalGroup);
        if (animalGroup != null) {
            createEnemyEntities(animalGroup, player);
            for (Entity enemy : entities) {
                if (enemy.getComponent(CombatStatsComponent.class) != null) {
                    GridPoint2 randomPos = new GridPoint2(
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x + 1),
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y + 1)
                    );
                    SpawnEnemyEntity(area, enemy, randomPos);
                }
            }
        }
        //makeAllAnimalDead();
    }

    @Override
    public void spawn(Entity player, MainGameArea area) {
        super.spawn(player, area);

        if (!isRoomCompleted) {
            logger.info("spawning enemies");
            this.spawnAnimals(area, player, this.minGridPoint, this.maxGridPoint);

            logger.info("spawning items");
            this.spawnItems();
        }
    }

    @Override
    public void removeRoom() {
        super.removeRoom();
        enemies.clear();
    }

    protected void SpawnEnemyEntity(MainGameArea area, Entity enemy, GridPoint2 position) {
        enemy.getEvents().addListener("checkAnimalsDead", () -> checkIfRoomComplete());
        this.spawnAnimalEntity(area, enemy, position);
        enemies.add(enemy);
    }
}
