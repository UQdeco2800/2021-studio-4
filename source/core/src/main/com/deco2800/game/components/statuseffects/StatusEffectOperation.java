package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.StatusEffectsController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;

import java.util.ArrayList;
import java.util.Timer;

public class StatusEffectOperation {
    private int type, boost, statOriginal;
    private String statusEffect;
    private Entity player;
    private ArrayList<String> statusEffects;

    /**
     * To save the original values to be saves.
     * Format:
     * Index:
     * 0: Speed
     * 1: Jump
     * 2: timeStop and interference time
     */
    private static ArrayList<Integer> originalValues = new ArrayList<>(3);

    public StatusEffectOperation(Entity player, String statusEffect, ArrayList<String> statusEffects) {
        this.player = player;
        this.statusEffect = statusEffect;
        this.statusEffects = statusEffects;

        /**
         * Initialises all values to 0 only the first time
         */
        for (int i = 0; i < 10; i++) {
            try {
                originalValues.get(i);
            } catch (IndexOutOfBoundsException e) {
                originalValues.add(i, 0);
            }
        }
    }

    public void inspect() {
        switch (statusEffect) {
            case "Buff_Jump":
                jumpBoost();
                break;
            case "Buff_Time_Stop": // Will not implement yet. Need to get Voids Entity
                break;
            case "Buff_Speed":
                speedChange(1);
                break;
            case "Debuff_Bomb": // Try to finish tomorrow
                break;
            case "Debuff_Speed":
                speedChange(-1);
                break;
            case "Debuff_Stuck":
                stuckInMud();
                break;
            default:
                break;
        }
    }

    /**
     * Maintains only one statusEffect in the array list at a time
     */
    private void singleStatusEffectCheck() { // NEEDS CORRECTING
        StatusEffectsController controller = new StatusEffectsController(player, statusEffect);
        int itr = 0;
         for (String status : statusEffects) {
             itr++;
             if (status.equals("") || status.equals(statusEffect)) {
                 if (status.equals(statusEffect)) {
                     controller.addStatusEffect(itr, statusEffect);
                 }
             } else {
                 controller.addStatusEffect(itr, "");
             }
         }
    }

    /**
     * changes the speed of the player
     * @param type Whether it is a Buff or DeBuff
     * @return the new speed of the player
     */
    /* Changed the method to be public for testing. Originally private. */
    public int speedChange(int type) { // Returns int for testing in possible future
        int speedBoost = StatusEffectEnum.SPEED.getStatChange(); // Must be smaller than 10

        int statOriginal;
        // Makes sure statOriginal is always the same value
        if (originalValues.get(0) == 0) {
            statOriginal = (int) player.getComponent(PlayerActions.class).getSpeed();
        } else {
            statOriginal = originalValues.get(0);
        }

        if(type == 1) {
       //     player.getEvents().trigger("setPowerUpAnimation", "SpeedUp");
        } else {
      //      player.getEvents().trigger("setPowerUpAnimation", "SpeedDown");
        }

        int newSpeed = StatusEffectEnum.SPEED.statChange(type, speedBoost, statOriginal);

        originalValues.add(0, statOriginal);

        singleStatusEffectCheck();
        int changedSpeed = player.getComponent(PlayerActions.class).alterSpeed(newSpeed);

        // Alters the speed back to the original setting after a certain time duration.
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        player.getComponent(PlayerActions.class).alterSpeed(-changedSpeed);
                      //  player.getEvents().trigger("setPowerUpAnimation", "Default");
                        // close the thread
                        t.cancel();
                    }
                },
                StatusEffectEnum.SPEED.getStatDuration()
        );

        if (player.getComponent(CombatStatsComponent.class).isDead()) {
            player.getComponent(PlayerActions.class).alterSpeed(-changedSpeed);
        }
        return changedSpeed;
    }

    /* Changed the method to be public for testing. Originally private. */
    public int jumpBoost() {
        int jumpBoost = StatusEffectEnum.JUMPBUFF.getStatChange(); // Must be smaller than 10

        singleStatusEffectCheck();
        int changedJumpHeight = player.getComponent(PlayerActions.class).alterJumpHeight(jumpBoost);
        player.getEvents().trigger("setPowerUpAnimation", "JumpUp");

        // Alters the speed back to the original setting after a certain time duration.
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        player.getComponent(PlayerActions.class).alterJumpHeight(-changedJumpHeight);
                      //  player.getEvents().trigger("setPowerUpAnimation", "Default");
                        // close the thread
                        t.cancel();
                    }
                },
                StatusEffectEnum.JUMPBUFF.getStatDuration()
        );

        if (player.getComponent(CombatStatsComponent.class).isDead()) {
            player.getComponent(PlayerActions.class).alterJumpHeight(-changedJumpHeight);
        }
        return changedJumpHeight;
    }

    /* Changed the method to be public for testing. Originally private. */
    public void stuckInMud() {
        GameTime gameTime = new GameTime();
        int currentSpeed = (int) player.getComponent(PlayerActions.class).getSpeed();
        int newSpeed = currentSpeed * -1;
       // System.out.println(gameTime.getTime());
        player.getComponent(PlayerActions.class).alterSpeed(newSpeed);
       // player.getEvents().trigger("setPowerUpAnimation", "Stuck");

        // Sets delay of 3 seconds before restoring the previous player speed.
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        player.getComponent(PlayerActions.class).alterSpeed(currentSpeed);
                        //player.getEvents().trigger("setPowerUpAnimation", "Default");
                        // close the thread
                        t.cancel();
                    }
                },
                StatusEffectEnum.STUCKINMUD.getStatDuration()
        );
    }

    private void FreezeVoid() { // Could do later
    // Hard to initialise the void's entity
    }
}
