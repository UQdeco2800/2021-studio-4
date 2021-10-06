package com.deco2800.game.components.scores;

public class CalcScore {

    /**
     * Returns a score based off of the time it took the player to complete the level
     * If the player does not complete the level before a specific time, their score
     * will default to 100
     * @param clock - in seconds
     * @return A score which is inversely proportional to the clocks time
     */
    public int calculateScore(int clock) {

        if (clock < 0) {
            throw new IllegalArgumentException("negative clock value was obtained");
        }

        int levelMaxTime = 3 * 60; // The player has to completed the level within 3 minutes
        int levelScore = 100; // The player will be awarded of 100 points per remaining second

        int secondsLeftOver = levelMaxTime - clock;

        // You can't ever get a score of 0. (Since 0 score turns into a '-' when read from textfile)
        return Math.max(1, secondsLeftOver) * levelScore;
    }
}
