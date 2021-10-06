package com.deco2800.game.components.levelselect;

import com.deco2800.game.levels.LevelDefinition;

public class PreviousLevel {
    private static LevelDefinition previousLevel; // Represents the most recent level the player has played.

    public PreviousLevel() {

    }

    /**
     * Update the most recent level played.
     */
    public void updatePreviousLevel(LevelDefinition levelPlayed) {
        previousLevel = levelPlayed;
    }

    /**
     * Gets the previous level.
     * @return previous level.
     */
    public LevelDefinition getPreviousLevel() {
        return previousLevel;
    }
}
