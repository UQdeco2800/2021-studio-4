package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;

public enum StatusEffectEnum implements StatusEffectInterface {
    SPEED (5, 4000, "SPEEDBUFF") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            int statChange;
            if (type == 1) { // For Buff
                statChange = statOriginal - boost;
            } else { // For DeBuff
                statChange = type * (statOriginal - boost);
            }
            return statChange;
        }
    },
    JUMPBUFF (200, 4000, "JUMPBUFF") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            return statOriginal;
        }
    },
    VOIDFREEZE (0, 0, "VOIDFREEZE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            int statChange = type * boost + statOriginal;
            return statChange;
        }
    },
    INTERFERANCE (0, 0, "INTERFERANCE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            int statChange = type * boost + statOriginal;
            return statChange;
        }
    },
    STUCKINMUD (0, 3000, "INTERFERANCE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            int statChange = type * boost + statOriginal;
            return statChange;
        }
    };

    /** statChange is the a numeric value for the change in stat for the StatusEffect.
     It is general, and it is up to the player class to determine what stat is being changed */
    private int statChange;
    /** Determines the duration of the StatusEffect */
    private int statDuration;
    /** Specifies the type of StatusEffect */
    private String Type;

    StatusEffectEnum (int statChange, int statDuration, String Type) {
        this.statChange = statChange;
        this.statDuration = statDuration;
        this.Type = Type;
    }

    public String getType() {
        return this.Type;
    }

    public int getStatChange() {
        return this.statChange;
    }

    public int getStatDuration() {
        return this.statDuration;
    }

}
