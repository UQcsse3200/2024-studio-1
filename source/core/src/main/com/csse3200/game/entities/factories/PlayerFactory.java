package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.csse3200.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
      FileLoader.readClass(PlayerConfig.class, "configs/player.json");


  private static final String[] playerTextures = {
          "images/player/player.png"
  };

  private static final String[] playerTextureAtlas = {
          "images/player/player.atlas"
  };

  private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);
  private static TextureAtlas atlas;

  /**
   * Create a player entity.
   * @return entity
   */
  public static Entity createPlayer() {

    loadAssets();
    
    atlas = new TextureAtlas(playerTextureAtlas[0]);
    TextureRegion defaultTexture = atlas.findRegion("idle");

    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player/player.atlas", TextureAtlas.class));

    animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk-left", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk-up", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk-right", 0.2f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk-down", 0.2f, Animation.PlayMode.LOOP);

    InventoryComponent inventoryComponent = new InventoryComponent();
    
    Entity player = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(inventoryComponent)
            .addComponent(inputComponent)
            .addComponent(new PlayerStatsDisplay())
            .addComponent(animator)
            .addComponent(new PlayerAnimationController());
            .addComponent(new PlayerInventoryDisplay(inventoryComponent))
            ;

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);

    player.setScale(1f, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());

    return player;
  }

  private static void loadAssets() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(playerTextures);
    resourceService.loadTextureAtlases(playerTextureAtlas);

    while (!resourceService.loadForMillis(10)) {
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
