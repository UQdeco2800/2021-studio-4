package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import java.util.*;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class StatusEffectTest {

//    private StatusEffectOperation jumpBuff;
//    private StatusEffectOperation speedBuff;
//    private StatusEffectOperation stuckInTheMud;
    private Entity player;
    private CombatStatsComponent combatStatsComponentNotDead;
    private CombatStatsComponent combatStatsComponentIsDead;
    float expected;
    float result;
    int type;
    private PlayerActions playerActions;
    private ArrayList<String> statusEffectList = new ArrayList<>();

    /**
     * Waits for the callable stat change of the jump buff to return the player's jump stat
     * back to normal.
     * @return a callable that satisfies the above condition.
     */
    private Callable jumpStatChangedBack() {
        return new Callable() {
          public Boolean call() throws Exception {
              return playerActions.getJumpHeight() == 300f; // The condition that must be fulfilled in order for the await to stop.
          }
        };
    }

    /**
     * Waits for the callable stat change of the speed buff to return the player's speed stat
     * back to normal.
     * @return a callable that satisfies the above condition.
     */
    private Callable speedStatChangedBack() {
        return new Callable() {
            public Boolean call() throws Exception {
                return playerActions.getSpeed() == 10f; // The condition that must be fulfilled in order for the await to stop.
            }
        };
    }

    private void assertTestCase(float expected, float result) {
        assertEquals(expected, result);
        assertTrue(expected == result);
        assertFalse(expected != result);
        assertNotEquals(expected + 1, result);
    }

    @BeforeEach
    public void initialiseClasses() {
        playerActions = new PlayerActions();
        /* The health determines whether the unit is dead. 1 = alive, 0 = dead */
        combatStatsComponentIsDead = new CombatStatsComponent(0,0);
        combatStatsComponentNotDead = new CombatStatsComponent(1,0);

        /* Reset the original speed of the player. This is mathematically done as there is no setter method */
        float speed = playerActions.getSpeed();
        int originalSpeed;
        if (speed != 10f) {
            originalSpeed = 10 - (int) speed;
            playerActions.alterSpeed(originalSpeed);
        }

        /* Reset the original height of the player */
        float height = playerActions.getJumpHeight();
        int originalHeight;
        if (height != 300f) {
            originalHeight = 300 - (int) height;
            playerActions.alterJumpHeight(originalHeight);
        }
    }

    @BeforeEach
    public void mockClasses() {
        /* Mocking classes */
        player = Mockito.mock(Entity.class);
    }

    @BeforeEach
    public void initialiseArray() {
        /* Initialise the array */
        statusEffectList.add(0,"Buff_Jump");
        statusEffectList.add(1,"Buff_Time_Stop");
        statusEffectList.add(2,"Buff_Speed");
        statusEffectList.add(3,"Debuff_Bomb");
        statusEffectList.add(4,"Debuff_Speed");
        statusEffectList.add(5,"Debuff_Stuck");
    }

//    @BeforeEach
//    public void initialiseStatusEffectOperationClasses() {
//        /* Initialise the buff and de-buff classes. Also redefining some methods for mocking purposes */
//        speedBuff = new StatusEffectOperation(player, "Buff_Speed",  statusEffectList) {
//            /* The speedChange() has been rewritten to change the stats in the playerActions class.
//                 This is because the player Entity is a mock class, meaning that when we call functions that rely on
//                 other classes, those classes will return null because it does not have them initialised,
//                 i.e. player.getComponents(PlayerActions.class).getSpeed() will fail because
//                 player.getComponent(PlayerActions.class) is null. The last JUnit test demonstrates this.
//                 To solve this, we create a PlayerActions object and directly implement the changes on the object whenever
//                 the player entity calls a function that affects PlayerActions. This is why when we call getSpeed(),
//                 we change the PlayerActions object manually, the same way as it would be done in practice; only difference
//                 is that in practice, the player Entity and PlayerActions object are explicitly linked, here they are not due to mocking.
//                 */
//            @Override
//            public int applySpeedBoost(int type) {
//                int statOriginal = (int) player.getComponent(PlayerActions.class).getSpeed();
//                int newSpeed = StatusEffectEnum.SPEED.statChange(type, StatusEffectEnum.SPEED.getStatChange(), statOriginal);
//                //System.err.println("" + "Player speed before change = " + player.getComponent(PlayerActions.class).getSpeed());
//                playerActions.alterSpeed(newSpeed);
//                //System.err.println("" + "Player speed after change = " + player.getComponent(PlayerActions.class).getSpeed());
//                if (player.getComponent(CombatStatsComponent.class).isDead()) {
//                    playerActions.alterSpeed(-newSpeed);
//                } else {
//                    Timer speedBoostDuration = new Timer();
//                    speedBoostDuration.schedule(
//                            new TimerTask() {
//                                @Override
//                                public void run() {
//                                    //System.err.println("" + "alterSpeed = " + -newSpeed);
//                                    //System.err.println("" + "Player speed before revert = " + playerActions.getSpeed());
//                                    player.getComponent(PlayerActions.class).alterSpeed(-newSpeed);
//                                    //System.err.println("" + "Player speed after revert = " + playerActions.getSpeed());
//                                    speedBoostDuration.cancel();
//                                }
//                            },
//                            StatusEffectEnum.SPEED.getStatDuration()/25
//                    );
//                }
//                return StatusEffectEnum.SPEED.getStatChange();
//            }
//        };
//
//        jumpBuff = new StatusEffectOperation(player, "Buff_Jump",  statusEffectList) {
//            @Override
//            public int applyJumpBoost() {
//                playerActions.alterJumpHeight(StatusEffectEnum.JUMPBUFF.getStatChange());
//                if (player.getComponent(CombatStatsComponent.class).isDead()) {
//                    playerActions.alterJumpHeight(-StatusEffectEnum.JUMPBUFF.getStatChange());
//                } else {
//                    Timer jumpBuffDuration = new java.util.Timer();
//                    jumpBuffDuration.schedule(
//                            new java.util.TimerTask() {
//                                @Override
//                                public void run() {
//                                    player.getComponent(PlayerActions.class).alterJumpHeight(-StatusEffectEnum.JUMPBUFF.getStatChange());
//                                    jumpBuffDuration.cancel();
//                                }
//                            },
//                            StatusEffectEnum.JUMPBUFF.getStatDuration()/25 /* For testing purposes, the time we wait is significantly lessened */
//                    );
//                }
//                return StatusEffectEnum.JUMPBUFF.getStatChange();
//            }
//        };
//
//        stuckInTheMud = new StatusEffectOperation(player, "Debuff_Stuck",  statusEffectList) {
//            @Override
//            public void stuckInMud() {
//                int newSpeed = -1 * (int) player.getComponent(PlayerActions.class).getSpeed();
//                player.getComponent(PlayerActions.class).alterSpeed(newSpeed);
//
//                Timer debuffDuration = new java.util.Timer();
//                debuffDuration.schedule(
//                        new java.util.TimerTask() {
//                            @Override
//                            public void run() {
//                                player.getComponent(PlayerActions.class).alterSpeed(-newSpeed);
//                                debuffDuration.cancel();
//                            }
//                        },
//                        StatusEffectEnum.STUCKINMUD.getStatDuration()/25 /* 4/25 seconds */
//                );
//            }
//        };
//    }
//
//    @Test
//    public void testSpeedBuffNotDead() {
//        //System.err.println("testSpeedBuffNotDead()");
//        type = 1;
//
//        /* Initialise method functionality */
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//
//        /* Test that the current speed remains true to the original */
//        expected = 10;
//        result =  player.getComponent(PlayerActions.class).getSpeed();
//        assertTestCase(expected, result);
//
//        /* Check that the change in stat is correct */
//        expected = StatusEffectEnum.SPEED.getStatChange();
//        result = speedBuff.applySpeedBoost(type);
//        assertTestCase(expected, result);
//
//        /* Check the change in stat */
//        expected = 15f;
//        result = player.getComponent(PlayerActions.class).getSpeed();
//        assertTestCase(expected, result);
//        //System.err.println("testSpeedBuffNotDead()\n");
//    }
//
//    @Test
//    public void testSpeedBuffIsDead() {
//        //System.err.println("testSpeedBuffIsDead()");
//        type = 1;
//        /* Tests the condition that the player is dead and got the power up */
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentIsDead);
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//
//        speedBuff.applySpeedBoost(type); /* Call the speed change */
//
//        expected = 10f;
//        result = player.getComponent(PlayerActions.class).getSpeed();
//        assertTestCase(expected, result);
//        //System.err.println("testSpeedBuffIsDead()\n");
//    }
//
//    @Test
//    public void testJumpBoostNotDead() {
//        /* Define the function actions */
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//
//        /* Check that the current jumpHeight remains true to the original */
//        expected = 300f;
//        result = player.getComponent(PlayerActions.class).getJumpHeight();
//        assertTestCase(expected, result);
//
//        /* Check that the jumpBoost amount is correct */
//        expected = StatusEffectEnum.JUMPBUFF.getStatChange();
//        result = jumpBuff.applyJumpBoost();
//        assertTestCase(expected, result);
//
//        /* Check that the jump height has changed */
//        expected = 500f;
//        result = player.getComponent(PlayerActions.class).getJumpHeight();
//        assertTestCase(expected, result);
//    }
//
//    @Test
//    public void testJumpBoostIsDead() {
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentIsDead);
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        jumpBuff.applyJumpBoost();
//
//        /* Check that the jump height does not change because the player is dead */
//        expected = 300f;
//        result = player.getComponent(PlayerActions.class).getJumpHeight();
//        assertTestCase(expected, result);
//    }
//
//    @Test
//    public void testSpeedDebuffNotDead() {
//        //System.err.println("testSpeedDebuffNotDead()");
//        type = -1;
//        /* Initialise method functionality */
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//
//        /* Test that the stat change is as expected */
//        expected = 5;
//        result = speedBuff.applySpeedBoost(type);
//        assertTestCase(expected, result);
//
//        /* Test that the player stat decreased */
//        expected = 5;
//        result = player.getComponent(PlayerActions.class).getSpeed();
//        assertTestCase(expected, result);
//        //System.err.println("testSpeedDebuffNotDead()\n");
//    }
//
//    @Test
//    public void testSpeedDebuffIsDead() {
//        //System.err.println("testSpeedDebuffIsDead()");
//        type = -1;
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentIsDead);
//
//        speedBuff.applySpeedBoost(type);
//
//        expected = 10f;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//        //System.err.println("testSpeedDebuffIsDead()\n");
//    }
//
//    @Test
//    public void testStuckInTheMudDebuffNotDead() {
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//        stuckInTheMud.stuckInMud();
//
//        expected = 0;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//    }
//
//    @Test public void testStuckInTheMudDebuffIsDead() {
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentIsDead);
//        stuckInTheMud.stuckInMud();
//
//        expected = 0;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//    }
//
//    @Test
//    public void testJumpBuffDelay() {
//        /* Define the function actions */
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        /* Apply the buff */
//        jumpBuff.applyJumpBoost();
//
//        /* Check that the stat changed. */
//        expected = 500f;
//        result = playerActions.getJumpHeight();
//        assertTestCase(expected, result);
//
//        /* Wait for the stat to change back */
//        await().until(jumpStatChangedBack());
//        expected = 300f;
//        result = playerActions.getJumpHeight();
//        assertTestCase(expected, result);
//    }
//
////    @Test
////    public void testSpeedBuffDelay() {
////        System.err.println("testSpeedBuffDelay()");
////        type = 1;
////        /* Define the function actions */
////        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
////        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
////        /* Apply the debuff */
////        speedBoost.speedChange(type);
////
////        /* Check that the stat changed. */
////        expected = 15f;
////        result = playerActions.getSpeed();
////        assertTestCase(expected, result);
////
////        /* Wait for the stat to change back */
////        try {
////            await().until(speedStatChangedBack());
////            expected = 10f;
////            result = playerActions.getSpeed();
////            assertTestCase(expected, result);
////        } catch(org.awaitility.core.ConditionTimeoutException e) {
////            System.err.println("" + "inside try-catch block player stat = " + playerActions.getSpeed());
////            System.err.println("" + "exception = " + e);
////        }
////        System.err.println("testSpeedBuffDelay()\n");
////    }
//
//    @Test
//    public void testSpeedDebuffDelay() {
//        //System.err.println("testSpeedDebuffDelay()");
//        type = -1;
//        /* Define the function actions */
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        /* Apply the debuff */
//        speedBuff.applySpeedBoost(type);
//
//        /* Check that the stat changed. */
//        expected = 5f;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//
//        /* Wait for the stat to change back */
//        await().until(speedStatChangedBack());
//        expected = 10f;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//        //System.err.println("testSpeedDebuffDelay()\n");
//    }
//
//    @Test
//    public void testStuckInTheMudDebuffDelay() {
//        /* Define the function actions */
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);
//
//        /* Apply debuff */
//        stuckInTheMud.stuckInMud();
//
//        /* Check that the stat changed. */
//        expected = 0f;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//
//        /* Wait for the stat to change back */
//        await().until(speedStatChangedBack());
//        expected = 10f;
//        result = playerActions.getSpeed();
//        assertTestCase(expected, result);
//    }

//    /* Uncomment if you want to see how the null works */
//    @Test
//    public void testNullClasses() {
//        System.err.println("" + "Result of player.getComponent(PlayerActions.class) = " + player.getComponent(PlayerActions.class)); // This will print null.
//
//        try {
//            System.err.println(player.getComponent(PlayerActions.class).getSpeed()); // This is trying to access something that is null
//        } catch (NullPointerException e) {
//            System.err.println("" + "Result of player.getComponent(PlayerActions.class).getSpeed() = " + e);
//            System.err.println("This is the syntax of the above code: null.getSpeed()");
//            System.err.println("It can be seen that getSpeed() is being called on a null value, hence the exception.");
//        }
//    }
}
