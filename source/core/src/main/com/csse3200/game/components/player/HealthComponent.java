package com.csse3200.game.components.player;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;

public class HealthComponent extends Component {
    private float current_health;
    private float max_health;

    public HealthComponent(float max_health) {
        this.max_health = max_health;
        this.current_health = max_health;
    }

    @Override
    public void create(){
        super.create();
        entity.getEvents().addListener("SmallHealthBoost",this::increaseHealth);
    }

    public void setHealth(float current_health){
        this.current_health = Math.min(current_health, max_health);
    }

    public void increaseHealth(float amount){
        setHealth(current_health + amount);
    }
}
