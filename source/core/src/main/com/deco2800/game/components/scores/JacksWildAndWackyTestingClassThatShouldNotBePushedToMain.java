package com.deco2800.game.components.scores;

import java.text.DecimalFormat;

public class JacksWildAndWackyTestingClassThatShouldNotBePushedToMain {

    private int clear_time = 20; //Clear time of level in seconds. Would be returned by Game at player reaching the
                                 //Victory screen when fully implemented. After crossing the Ideal Clear Time * 60
                                 //threshold, no score above 0000 is achievable.
    //private int clock; //Unsure if needed.
    //private double score_multiplier; //Irrelevant. Score Multiplier and Clear Time are the same thing.
    private int score_base = 1; //Base for all score calculations.

    private int ideal_clear_time = 7; //Represents amount of minutes before score is locked at 0000 on that level.
    private int zero_requiem = (ideal_clear_time * 60) / 100; //Uses the set amount of minutes before earning a 0000
                                                              //and makes it into a one one-hundredth of the total
                                                              //seconds in those minutes.

    public JacksWildAndWackyTestingClassThatShouldNotBePushedToMain () {

    }

    private int score_Calculator() {

        DecimalFormat decFormat = new DecimalFormat("#.###"); //Will cut the unrounded score to 3 decimal places
                                                                     //so it can be multipled to always give a number
                                                                     //ending in a 0.

        /**
         for (clock = 0; clock < clear_time; clock++) {
            score_multiplier = score_multiplier + 1;
         }

         ^ Think it's irrelevant but haven't deleted as not certain, yet.
         */

        String score_unrounded = String.valueOf((score_base - (clear_time / zero_requiem) * 0.01));
                                                                                          //Not sure why the 0.01 is
                                                                                          //needed, but any other value
                                                                                          //breaks the calculations.

        int score = Integer.parseInt(decFormat.format(score_unrounded)) * 10000; //Rounds score to three decimal places
                                                                                 //so score format should always be
                                                                                 // ###0. (4 digits ending with a zero)

        System.out.println(score);
        return score;
    }
}
