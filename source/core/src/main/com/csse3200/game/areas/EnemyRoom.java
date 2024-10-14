package com.csse3200.game.areas;

import static com.csse3200.game.services.ServiceLocator.getGameAreaService;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;

import com.csse3200.game.utils.RandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class representing a room containing enemies in the game.
 * This class is used as a base for both standard enemy rooms and boss rooms.
 * It extends BaseRoom and adds functionality specific to rooms with hostile entities.
 */
public abstract class EnemyRoom extends BaseRoom {
    /** Logger for this class. */
    private static final Logger logger = LoggerFactory.getLogger(EnemyRoom.class);

    /** Factory for creating NPC entities, including enemies and bosses. */
    protected final NPCFactory npcFactory;

    /** Group index for animals/enemies in the room. */
    protected final int animalGroup;

    /** List of animal/enemy specifications for the room. */
    protected List<List<String>> animalSpecifications;

    /** Flag indicating if this is a boss room. */
    protected boolean isBossRoom = false;

    /** List of enemy entities in the room. */
    private List<Entity> enemies = new ArrayList<>();
    private List<String> createItemSpecs = new ArrayList<>();

    /**
     * Constructs a new EnemyRoom.
     *
     * @param npcFactory Factory for creating NPC entities.
     * @param collectibleFactory Factory for creating collectible items.
     * @param terrainFactory Factory for creating terrain.
     * @param roomConnections List of room connections.
     * @param specification Room specification string.
     * @param roomName Name of the room.
     */
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
        this.createItemSpecs = createItemSpecs();

        List<String> split = List.of(specification.split(","));
        this.animalGroup = Integer.parseInt(split.get(4));
    }

    /**
     * Gets the list of enemy entities in the room.
     *
     * @return List of enemy entities.
     */
    public List<Entity> getEnemies() {
        return enemies;
    }


    /**
     * Spawns Deployable Entity
     */
    public void SpawnDeployable(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        entities.add(entity);
        getGameAreaService().getGameArea().spawnEntityAt(entity, tilePos, centerX, centerY);

    }

    /**
     * Checks if the room is complete by verifying if all animals/enemies are dead.
     * Sets the room as complete if the condition is met.
     */
    public void checkComplete() {
        if (isAllAnimalDead()) {
            System.out.println("room is complete");
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    spawnItems();
                }
            }, 1.0f);
            setComplete();
        }
    }

    /**
     * Gets the animal specifications for the room.
     * This method should be implemented by subclasses to define enemy types.
     *
     * @return A list of animal/enemy specification lists.
     */
    protected abstract List<List<String>> getAnimalSpecifications();

    private List<String> createItemSpecs() {
        List<String> items = new ArrayList<>();
        RandomNumberGenerator rng = ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass());
        int itemsGroup = 0;
        if (rng.getRandomInt(0, 1) == 1) {
            GameController gameController = ServiceLocator.getGameAreaService().getGameController();
            String name = gameController.getPlayer().getComponent(PlayerConfigComponent.class).getPlayerConfig().name;
            switch (name) {
                case "bear":
                    itemsGroup = 1;
            }
        }
        List<String> selectedItemsSpec = this.itemSpecifications.get(itemsGroup);
        items.add(selectedItemsSpec.get(rng.getRandomInt(0, selectedItemsSpec.size())));
        items.add(selectedItemsSpec.get(rng.getRandomInt(0, selectedItemsSpec.size())));
        return items;
    }

    /**
     * Creates enemy entities based on the provided specifications.
     *
     * @param animals List of animal/enemy specifications.
     * @param player The player entity.
     */
    protected void createEnemyEntities(List<String> animals, Entity player) {
        for (String animal : animals) {
            Entity enemy = npcFactory.create(animal, player);
            entities.add(enemy);
        }
    }

    /**
     * Sets all animals/enemies in the room to dead state.
     * Useful for debugging or special game events.
     */
    public void makeAllAnimalDead() {
        for (Entity entity : enemies) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null) {
                combatStatsComponent.setHealth(1);
                combatStatsComponent.hit(combatStatsComponent);
            }
        }
    }

    /**
     * Checks if all animals/enemies in the room are dead.
     *
     * @return true if all animals/enemies are dead, false otherwise.
     */
    public boolean isAllAnimalDead() {
        for (Entity entity : enemies) {
            CombatStatsComponent combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
            if (combatStatsComponent != null && !combatStatsComponent.isDead()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Spawns items in the room.
     * Items are placed at predefined positions.
     */
    private void spawnItems() {
        GameArea area = ServiceLocator.getGameAreaService().getGameArea();
        List<String> items = this.createItemSpecs;


        if (items != null && items.size() >= 2) {
            spawnItem(area, items.get(0), new GridPoint2(8, 8));
            spawnItem(area, items.get(1), new GridPoint2(6, 8));
        }
    }

    /**
     * Spawns animals/enemies in the room at random positions within the given bounds.
     *
     * @param area The game area to spawn the animals/enemies in.
     * @param player The player entity.
     * @param min The minimum grid point for spawning.
     * @param max The maximum grid point for spawning.
     */
    protected void spawnAnimals(GameArea area, Entity player, GridPoint2 min, GridPoint2 max) {
        List<String> animalGroup = this.animalSpecifications.get(this.animalGroup);
        if (animalGroup != null) {
            createEnemyEntities(animalGroup, player);
            for (Entity enemy : entities) {
                if (enemy.getComponent(CombatStatsComponent.class) != null) {
                    Vector2 playerPos = player.getPosition();
                    GridPoint2 randomPos = new GridPoint2(
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x),
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y)
                    );
                    while (Math.abs(randomPos.x-playerPos.x) <= 1 || Math.abs(randomPos.y-playerPos.y) <= 1){
                     randomPos = new GridPoint2(
                      ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x + 1),
                       ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y + 1)
                        );
                    }
                    spawnEnemyEntity(area, enemy, randomPos);
                }
            }
        }
    }

    /**
     * Spawns the room, including terrain, doors, enemies, and items.
     *
     * @param player The player entity.
     * @param area The game area to spawn the room in.
     */
    @Override
    public void spawn(Entity player, GameArea area) {
        super.spawn(player, area);

        if (!isComplete()) {
            logger.info("spawning enemies");
            this.spawnAnimals(area, player, this.minGridPoint, this.maxGridPoint);

            //logger.info("spawning items");
            //this.spawnItems();
        }
    }

    /**
     * Removes the room and clears all enemy entities.
     */
    @Override
    public void removeRoom() {
        super.removeRoom();
        enemies.clear();
    }

    /**
     * Spawns an enemy entity and sets up its event listener for room completion check.
     *
     * @param area The game area to spawn the enemy in.
     * @param enemy The enemy entity to spawn.
     * @param position The position to spawn the enemy at.
     */
    public void spawnEnemyEntity(GameArea area, Entity enemy, GridPoint2 position) {
        enemy.getEvents().addListener("checkAnimalsDead", () -> checkComplete());
        this.spawnAnimalEntity(area, enemy, position);
        enemies.add(enemy);
    }
}
