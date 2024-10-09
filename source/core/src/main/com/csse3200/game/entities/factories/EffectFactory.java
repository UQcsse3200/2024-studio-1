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
     * @param config        The effect configuration.
     * @param target  The entity to which the effect is applied.
     */
    public static void createEffectEntity(EffectConfig config, Entity target) {
        if (config.type == EffectType.KNOCKBACK) {
            logger.debug("No animation for knockback effect");
            return;
        }

        String effectType = config.type.name().toLowerCase();
        String atlasPath = String.format("images/effects/%s.atlas", effectType);
        TextureAtlas atlas = ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class);

        // Create the EffectEntity with the loaded atlas, effect name, and duration
        EffectEntity effectEntity = new EffectEntity(atlas, effectType, config.duration, target);
        ServiceLocator.getEntityService().register(effectEntity);

        logger.info("Created EffectEntity '{}' for Entity ID {}", effectType, target.getId());
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