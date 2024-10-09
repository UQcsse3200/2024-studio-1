package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.effects.*;
import com.csse3200.game.entities.EffectEntity;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EffectConfig;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create Effect instances based on configuration.
 */
public class EffectFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(EffectFactory.class);
    private static EffectFactory instance;

    private EffectFactory() {
        super(logger);
    }

    public static synchronized EffectFactory getInstance() {
        if (instance == null) {
            instance = new EffectFactory();
        }
        return instance;
    }

    /**
     * Creates an Effect instance based on the provided configuration and source entity.
     *
     * @param config        The effect configuration.
     * @param sourceEntity  The entity applying the effect.
     * @return An instance of Effect.
     */
    public static Effect createEffect(EffectConfig config, Entity sourceEntity) {
        return switch (config.type) {
            case STUN -> new StunEffect(config.duration);
            case POISON -> new PoisonEffect(config.damagePerSecond, config.duration);
            case KNOCKBACK -> new KnockbackEffect(sourceEntity, config.force);
        };
    }

    /**
     * Creates an EffectEntity based on the provided configuration and source entity.
     *
     * @param target            The entity to which the effect is applied.
     * @param type              The type of effect to create.
     * @param duration          Duration of the effect in seconds.
     * @param disposeCallback   Callback to run when the effect entity is disposed.
     */
    public static void createOrUpdateEffectEntity(Entity target, EffectType type, float duration,
                                                  Runnable disposeCallback) {
        if (type == EffectType.KNOCKBACK) {
            logger.debug("No animation for knockback effect");
            return;
        }

        String effectType = type.name().toLowerCase();
        String atlasPath = String.format("images/effects/%s.atlas", effectType);

        // Load the effect atlas
        try {
            ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class);
        } catch (Exception e) {
            logger.error("Failed to load effect atlas for '{}'", effectType);
            return;
        }
        TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class);

        // Create the EffectEntity with the loaded atlas, effect name, and duration
        EffectEntity effectEntity = new EffectEntity(atlas, effectType, duration, target, disposeCallback);
        ServiceLocator.getEntityService().register(effectEntity);

        logger.info("Created EffectEntity '{}' for Entity ID {}", effectType, target.getId());

        // Associate the EffectEntity with the target's EffectComponent
        EffectComponent effectComponent = target.getComponent(EffectComponent.class);
        if (effectComponent != null) {
            effectComponent.setEffectEntity(effectEntity);
        }
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        return new String[] {
            "images/effects/stun.atlas", "images/effects/poison.atlas"
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[] {
            "images/effects/stun.png", "images/effects/poison.png"
        };
    }
}