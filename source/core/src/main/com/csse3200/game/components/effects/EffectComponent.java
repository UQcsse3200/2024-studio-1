package com.csse3200.game.components.effects;

import com.csse3200.game.components.Component;
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

    @Override
    public void update() {
        float deltaTime = ServiceLocator.getTimeSource().getDeltaTime();
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.update(entity, deltaTime);
            if (effect.isExpired()) {
                effect.remove(entity);
                iterator.remove();
                logger.debug("Effect {} expired and removed from entity {}", effect.getClass().getSimpleName(), entity);
            }
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
                logger.debug("Effect {} refreshed on entity {}", effect.getClass().getSimpleName(), entity);
                return;
            }
        }

        effect.apply(entity);
        activeEffects.add(effect);
        logger.debug("Effect {} applied to entity {}", effect.getClass().getSimpleName(), entity);
    }

    /**
     * Checks if a specific effect type is active.
     *
     * @param type The type of effect.
     * @return True if the effect is active, false otherwise.
     */
    public boolean hasEffect(EffectType type) {
        return activeEffects.stream().anyMatch(effect -> effect.getType() == type);
    }
}