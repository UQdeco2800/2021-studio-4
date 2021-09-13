package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.npc.StatusEffectsController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;

import java.util.ArrayList;

public class StatusEffectOperation {
    private int type, boost, statOriginal;
    private String statusEffect;
    private Entity player;
    private ArrayList<String> statusEffects;

    public StatusEffectOperation(Entity player, String statusEffect, ArrayList<String> statusEffects) {
        this.player = player;
        this.statusEffect = statusEffect;
        this.statusEffects = statusEffects;
    }

    public void inspect() {
        switch (statusEffect) {
            case "Buff_Jump":
                break;
            case "Buff_Time_Stop":
                break;
            case "Buff_Speed":
                speedChange(1);
                break;
            case "Debuff_Bomb":
                break;
            case "Debuff_Speed":
                speedChange(-1);
                break;
            case "Debuff_Stuck":
                break;
            default:
                break;
        }
    }

    /**
     * changes the speed of the player
     * @param type Whether it is a Buff or DeBuff
     * @return the new speed of the player
     */
    private int speedChange(int type) { // Returns int for testing possible in future
        int speedBoost = 5; // Must be smaller than 10
        int statOriginal = (int) player.getComponent(PlayerActions.class).getSpeed();

        // Makes sure the statOriginal is always the same value
//        if (statusEffects.contains("Debuff_Speed") && statusEffects.contains("Buff_Speed")) {
//
//            if (statusEffects.contains("Debuff_Speed")) {
//                statOriginal = statOriginal + speedBoost;
//            } else {
//                statOriginal = statOriginal - speedBoost;
//            }
//        }

        int newSpeed = StatusEffectEnum.SPEED.statChange(type, speedBoost, statOriginal);

        StatusEffectsController controller= new StatusEffectsController(player, statusEffect);

        return player.getComponent(PlayerActions.class).alterSpeed(newSpeed);
    }
}
