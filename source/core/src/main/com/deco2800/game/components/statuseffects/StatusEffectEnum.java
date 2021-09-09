package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;

public enum StatusEffectEnum implements StatusEffectInterface {
    SPEEDBUFF (2, 10, "SPEEDBUFF") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {
            int statChange = type * boost;

        }
    },
    JUMPBUFF (2, 10, "JUMPBUFF") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

        }
    },
    TIMESTOP (0, 0, "TIMESTOP") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

        }
    },
    /*
    SPEEDDEBUFF (-2, 10, "SPEEDDEBUFF") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

        }
    },
     */
    INTERFERANCE (0, 0, "INTERFERANCE") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

        }
    },
    FREEZE (0, 0, "FREEZE") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

        }
    },
    RESET (0, 0, "RESET") {
        @Override
        public void boosts(int type, int boost, int statOriginal) {

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
