package com.csse3200.game.components.effects;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.EffectEntity;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Component to manage active effects on an entity.
 */
public class EffectComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(EffectComponent.class);
    private final List<Effect> activeEffects = new LinkedList<>();
    private EffectEntity effectEntity;

    @Override
    public void create() {
        super.create();
        EffectFactory.loadAssets();
        createOrUpdateEffectEntity();
    }

    @Override
    public void update() {
        float deltaTime = ServiceLocator.getTimeSource().getDeltaTime();
        Iterator<Effect> iterator = activeEffects.iterator();
        boolean effectChanged = false;

        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.update(entity, deltaTime);
            if (effect.isExpired()) {
                effect.remove(entity);
                iterator.remove();
                effectChanged = true;
                logger.debug("Effect {} expired and removed from entity {}", effect.getClass().getSimpleName(), entity);
            }
        }

        if (effectChanged) {
            createOrUpdateEffectEntity();
        }
    }

    /**
     * Adds a new effect to the entity.
     *
     * @param effect The effect to add.
     */
    public void addEffect(Effect effect) {
        // Check if an effect of the same type already exists
        for (Effect activeEffect : activeEffects) {
            if (activeEffect.getType() == effect.getType()) {
                // Refresh the existing effect's duration or stack if applicable
                activeEffect.refresh(effect);
                createOrUpdateEffectEntity();
                logger.debug("Effect {} refreshed on entity {}", effect.getClass().getSimpleName(), entity);
                return;
            }
        }

        effect.apply(entity);
        activeEffects.add(effect);
        createOrUpdateEffectEntity();
        logger.debug("Effect {} applied to entity {}", effect.getClass().getSimpleName(), entity);
    }

    /**
     * Creates or updates the single EffectEntity based on active effects.
     */
    private void createOrUpdateEffectEntity() {
        if (activeEffects.isEmpty()) {
            if (effectEntity != null) {
                effectEntity.dispose();
                effectEntity = null;
                logger.debug("EffectEntity disposed as no active effects remain.");
            }
            return;
        }

        if (effectEntity == null) {
            // Create a new EffectEntity based on the most recent effect
            EffectType primaryEffect = activeEffects.getLast().getType();
            float duration = activeEffects.getLast().getDuration();
            EffectFactory.createOrUpdateEffectEntity(entity, primaryEffect, duration,  this::onEffectEntityDisposed);
        }
    }

    /**
     * Callback when an EffectEntity is disposed.
     */
    private void onEffectEntityDisposed() {
        this.effectEntity = null;
        createOrUpdateEffectEntity();
    }

    /**
     * Sets the EffectEntity reference.
     *
     * @param effectEntity The EffectEntity to associate.
     */
    public void setEffectEntity(EffectEntity effectEntity) {
        this.effectEntity = effectEntity;
    }
}