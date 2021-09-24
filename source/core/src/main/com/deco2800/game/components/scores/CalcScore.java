package com.deco2800.game.components.scores;

public class CalcScore {

    /**
     * Returns a score based off of the time it took the player to complete the level
     * If the player does not complete the level before a specific time, their score
     * will default to 0
     *
     * @param clock - in seconds
     * @return A score which is proportional to the clocks time
     */
    public long calculateScore(int clock) {
        int levelMaxTime = 3 * 60; // The player has to completed the level within 3 minutes
        int levelScore = 100; // The player will be awarded of 100 points per remaining second
        int score; // The players calculated score

        int secondsLeftOver = levelMaxTime - clock;

        score = Math.max(0, secondsLeftOver) * levelScore;

        return score;
    }
}
