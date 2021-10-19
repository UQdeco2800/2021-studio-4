package com.deco2800.game.components.levelselect;

import com.deco2800.game.levels.LevelInfo;

public class PreviousLevel {
    private static LevelInfo previousLevel; // Represents the most recent level the player has played.

    public PreviousLevel() {

    }

    /**
     * Update the most recent level played.
     * @param  levelPlayed - the level played
     */
    public void updatePreviousLevel(LevelInfo levelPlayed) {
        previousLevel = levelPlayed;
    }

    /**
     * Gets the previous level.
     * @return previous level.
     */
    public LevelInfo getPreviousLevel() {
        return previousLevel;
    }
}
