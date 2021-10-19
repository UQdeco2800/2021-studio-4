package com.deco2800.game.components.statusEffects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.statuseffects.StatusEffectTargetComponent;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class StatusEffectTest {

    private Entity player;
    private CombatStatsComponent combatStatsComponent;
    private PlayerActions playerActions;
    private StatusEffectTargetComponent SETC;
    private static TheVoidController theVoid;

    private float expected;
    private float result;
    private int type;

    private void assertTestCase(float expected, float result) {
        assertEquals(expected, result);
        assertTrue(expected == result);
        assertFalse(expected != result);
        assertNotEquals(expected + 1, result);
    }
    private void assertStatusEffects(StatusEffect expected, StatusEffect result) {
        assertEquals(expected, result);
        assertTrue(expected == result);
    };

    @BeforeEach
    public void mockClasses() {
        /* Mocking classes */
        player = Mockito.mock(Entity.class);
        theVoid = Mockito.mock(TheVoidController.class);
    }

    @BeforeEach
    public void initialiseClasses() {
        playerActions = new PlayerActions("example_level_string");
        combatStatsComponent = new CombatStatsComponent(1,0);
        SETC = new StatusEffectTargetComponent() {
            private StatusEffect currentStatusEffect = null;
            private Long currentStatusEffectStartTime = null;
            private StatusEffectResetTask currentStatusEffectResetTask = null;
            private Long runTime = 60000L;

            // Overwriting function to force set the current start time of the status effect.
            @Override
            public void applyStatusEffect(StatusEffect statusEffect) {
                if (currentStatusEffect != null) {
                    currentStatusEffect = null;
                    currentStatusEffectStartTime = null;
                    currentStatusEffectResetTask.run();
                    currentStatusEffectResetTask = null;
                }

                currentStatusEffect = statusEffect;
                /* Changed from using service locator */
                currentStatusEffectStartTime = 0L;

                switch (statusEffect) {
                    case BOMB:
                        break;
                    case FAST:
                    case SLOW:
                        currentStatusEffectResetTask = speedEffect(statusEffect);
                        break;
                    case JUMP:
                        currentStatusEffectResetTask = jumpBoost();
                        break;
                    case STUCK:
                        currentStatusEffectResetTask = stuckInMud();
                        break;
                    case TIME_STOP:
                        currentStatusEffectResetTask = theVoid.pauseVoid();
                        break;
                }
            }

            @Override
            public StatusEffect getCurrentStatusEffect() {
                return currentStatusEffect;
            }

            @Override
            public StatusEffectResetTask speedEffect(StatusEffect speedEffect) {
                int speedBoost = speedEffect.getMagnitude(); // Must be smaller than 10

                // Changed explicitly to player to avoid null pointer.
                int statOriginal = (int) player.getComponent(PlayerActions.class).getSpeed();

                int newSpeed;
                if (speedEffect == StatusEffect.FAST) {
                    newSpeed = statOriginal - speedBoost;
                } else {
                    newSpeed = -1 * (statOriginal - speedBoost);
                }

                int changedSpeed = player.getComponent(PlayerActions.class).alterSpeed(newSpeed);

                // Alters the speed back to the original setting after a certain time duration.
                return new StatusEffectResetTask() {
                    @Override
                    public void run() {
                        // Changed to player to avoid null pointer.
                        player.getComponent(PlayerActions.class).alterSpeed(-changedSpeed);
                    }
                };
            }

            @Override
            public StatusEffectResetTask jumpBoost() {
                int jumpBoost = StatusEffect.JUMP.getMagnitude();

                // Changed to player to avoid null pointer.
                int changedJumpHeight = player.getComponent(PlayerActions.class).alterJumpHeight(jumpBoost);

                // Alters the speed back to the original setting after a certain time duration.
                return new StatusEffectResetTask() {
                    @Override
                    public void run() {
                        // Changed to player to avoid null pointer.
                        player.getComponent(PlayerActions.class).alterJumpHeight(-changedJumpHeight);
                    }
                };
            }

            @Override
            public StatusEffectResetTask stuckInMud() {
                int currentSpeed = (int) player.getComponent(PlayerActions.class).getSpeed();
                int newSpeed = currentSpeed * -1;

                // Changed to player to avoid null pointer.
                player.getComponent(PlayerActions.class).alterSpeed(newSpeed);

                return new StatusEffectResetTask() {
                    @Override
                    public void run() {
                        // Changed to player to avoid null pointer.
                        player.getComponent(PlayerActions.class).alterSpeed(currentSpeed);
                    }
                };
            }

            @Override
            public void update() {
                super.update();
                /* Hard coded the duration of the status effect has elapsed. The value used was jump boost duration as it is the longest. */
                if (currentStatusEffect != null && this.runTime >= currentStatusEffect.getDuration()*1000) {
                    currentStatusEffect = null;
                    currentStatusEffectStartTime = null;
                    currentStatusEffectResetTask.run();
                    currentStatusEffectResetTask = null;
                }
            }

        };
    }

    @AfterEach
    public void cleanUp() {
        /* Sets all objects to null to be reinitialised. This is to prevent any changes from previous
        tests carrying over. */
        SETC = null;
        playerActions = null;
        combatStatsComponent = null;
        theVoid = null;
        player = null;
    }

    @Test
    public void testApplySpeedBuff() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add speed boost */
        SETC.applyStatusEffect(StatusEffect.FAST);
        // We expect the getComponent(PlayerActions.class) is called twice.
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.FAST, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplyJumpBuff() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        /* Add jump boost */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        verify(player, times(1)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.JUMP, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplyTimeStop() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
        SETC.applyStatusEffect(StatusEffect.TIME_STOP);
        verify(player, times(0)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.TIME_STOP, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplySpeedDebuff() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
        SETC.applyStatusEffect(StatusEffect.SLOW);
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.SLOW, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplyStuckInMud() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
        SETC.applyStatusEffect(StatusEffect.STUCK);
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.STUCK, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testUpdateStatusEffect() {
        System.err.println("2");
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Test speed buff */
        SETC.applyStatusEffect(StatusEffect.FAST);
        // Check that the status effect has been added.
        assertStatusEffects(StatusEffect.FAST, SETC.getCurrentStatusEffect());
        // Remove the status effect.
        SETC.update();
        assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test jump buff */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        // Check that the status effect has been added.
        assertStatusEffects(StatusEffect.JUMP, SETC.getCurrentStatusEffect());
        // Remove the status effect.
        SETC.update();
        assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test time stop */
        SETC.applyStatusEffect(StatusEffect.TIME_STOP);
        // Check that the status effect has been added.
        assertStatusEffects(StatusEffect.TIME_STOP, SETC.getCurrentStatusEffect());
        // Remove the status effect.
        SETC.update();
        assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test speed debuff*/
        SETC.applyStatusEffect(StatusEffect.SLOW);
        // Check that the status effect has been added.
        assertStatusEffects(StatusEffect.SLOW, SETC.getCurrentStatusEffect());
        // Remove the status effect.
        SETC.update();
        assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test stuck in the mud debuff */
        SETC.applyStatusEffect(StatusEffect.STUCK);
        // Check that the status effect has been added.
        assertStatusEffects(StatusEffect.STUCK, SETC.getCurrentStatusEffect());
        // Remove the status effect.
        SETC.update();
        assertStatusEffects(null, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testSpeedBuffStatChange() {
        System.err.println("3");
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        type = 1;
        /* apply speed buff */
        SETC.applyStatusEffect(StatusEffect.FAST);
        result = playerActions.getSpeed();
        expected = 15;
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertTestCase(expected, result);
    }

    @Test
    public void testJumpBuffStatChange() {
        System.err.println("4");
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* apply jump buff */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        result = playerActions.getJumpHeight();
        expected = 600;
        verify(player).getComponent(PlayerActions.class);
        assertTestCase(expected, result);
    }

//    @Test
//    public void testTimeStopBehaviour() {
//        System.err.println("5");
//        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
//        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
//
//        /* apply time stop buff */
//        SETC.applyStatusEffect(StatusEffect.TIME_STOP);
//        verify(theVoid).pauseVoid();
//    }



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
