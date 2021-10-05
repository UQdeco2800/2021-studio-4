package com.deco2800.game.components.statuseffects;

public enum StatusEffectEnum implements StatusEffectInterface {
    SPEED (5, 7000, "SPEEDBUFF") {
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
    JUMPBUFF (200, 10000, " ") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            return statOriginal;
        }
    },
    VOIDFREEZE (0, 3000, "VOIDFREEZE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            return type * boost + statOriginal;
        }
    },
    INTERFERANCE (0, 0, "INTERFERANCE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            return type * boost + statOriginal;
        }
    },
    STUCKINMUD (0, 4000, "INTERFERANCE") {
        @Override
        public int statChange(int type, int boost, int statOriginal) {
            return type * boost + statOriginal;
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
