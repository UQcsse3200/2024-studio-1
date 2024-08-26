package com.csse3200.game.components.player;

import com.csse3200.game.components.CombatStatsComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Component used to store information relating to
 * player-specific combat elements, such as
 * invincibility frames and (eventually) potentially different weapons
 */
public class PlayerCombatComponent extends CombatStatsComponent {
    private boolean isInvincible;
    private static final int timeInvincible = 2000;
    private long lastTimeHit;
    private Timer timer;

    private class removeInvincible extends TimerTask {
        @Override
        public void run() {
            setInvincible(false);
        }
    };

    public PlayerCombatComponent(int health, int baseAttack){
        super(health, baseAttack);
        isInvincible = false;
        setLastTimeHit(System.currentTimeMillis());
        timer = new Timer();
    }

    public void setInvincible(boolean invincible){
        isInvincible = invincible;
    }

    public boolean getIsInvincible(){
        return isInvincible;
    }

    public void setLastTimeHit(long lastTimeHit) {
        this.lastTimeHit = lastTimeHit;
    }

    public long getLastTimeHit(){
        return lastTimeHit;
    }
    @Override
    public void hit(CombatStatsComponent attacker){
        System.out.println("I was hit");
        if (getIsInvincible() == false) {
            System.out.println("I was hurt");
            int newHealth = getHealth() - attacker.getBaseAttack();
            setHealth(newHealth);
            setInvincible(true);
            removeInvincible task = new removeInvincible();
            timer.schedule(task, 2000);
        } else {
            System.out.println("I am invincible");
        }
//        if (getLastTimeHit() < System.currentTimeMillis() - timeInvincible) {
//            System.out.println("I was hurt");
//            int newHealth = getHealth() - attacker.getBaseAttack();
//            setHealth(newHealth);
//            setInvincible(true);
//            setLastTimeHit(System.currentTimeMillis());
//        } else {
//            System.out.println("I am invincible");
//        }
    }
}
