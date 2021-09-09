package com.deco2800.game.components.statuseffects;

/**
 * The enums are the possible status effects that can be obtained from power-ups or power-down.
 */
public enum StatusEffect {
    SPEEDBUFF (2, 10, "SPEEDBUFF"),
    JUMPBUFF (2, 10, "JUMPBUFF"),
    TIMESTOP (0, 0, "TIMESTOP"),
    SPEEDDEBUFF (-2, 10, "SPEEDDEBUFF"),
    INTERFERANCE (0, 0, "INTERFERANCE"),
    STUCKINTHEMUD (0, 0, "STUCKINTHEMUD");

    /** statChange is the a numeric value for the change in stat for the StatusEffect.
     It is general, and it is up to the player class to determine what stat is being changed */
    private int statChange;
    /** Determines the duration of the StatusEffect */
    private int statDuration;
    /** Specifies the type of StatusEffect */
    private String Type;

    StatusEffect (int statChange, int statDuration, String Type) {
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
