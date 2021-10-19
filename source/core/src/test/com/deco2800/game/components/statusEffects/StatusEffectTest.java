package com.deco2800.game.components.statusEffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.statuseffects.StatusEffectTargetComponent;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class StatusEffectTest {

    private Entity player;
    private CombatStatsComponent combatStatsComponent;
    private PlayerActions playerActions;
    private StatusEffectTargetComponent SETC;

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
    }

    @BeforeEach
    public void mockClasses() {
        /* Mocking classes */
        player = Mockito.mock(Entity.class);
    }

    @BeforeEach
    public void initialiseClasses() {
        playerActions = new PlayerActions();
        combatStatsComponent = new CombatStatsComponent(1, 0);
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
                        currentStatusEffectResetTask = TheVoidController.pauseVoid();
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
                if (currentStatusEffect != null && this.runTime >= currentStatusEffect.getDuration() * 1000) {
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
    public void testSpeedBuffStatChange() {
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
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* apply jump buff */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        result = playerActions.getJumpHeight();
        expected = 600;
        verify(player).getComponent(PlayerActions.class);
        assertTestCase(expected, result);
    }

    @Test
    public void testUpdateStatusEffect() {
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
    public void testApplyStatusEffectBehaviour() {
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add speed boost */
        SETC.applyStatusEffect(StatusEffect.FAST); // Calls 2 times.
        SETC.update(); // Calls 1 time.
        verify(player, times(3)).getComponent(PlayerActions.class);

        /* Add another speed boost and then a jump boost. The number of times player.getComponents(PlayerActions) is
        called carries from the previous operation */
        SETC.applyStatusEffect(StatusEffect.FAST); // Calls 2 times.
        SETC.applyStatusEffect(StatusEffect.JUMP); // Calls 2 times because of update().
        verify(player, times(4 + 3)).getComponent(PlayerActions.class);

        /* Add two status effects then updating */
        SETC.applyStatusEffect(StatusEffect.FAST); // Calls 3 times (1 time to update() on the previous status effect
                                                   // which is still in effect).
        SETC.applyStatusEffect(StatusEffect.JUMP); // Calls 2 times because of update().
        SETC.update(); // Calls once.
        verify(player, times(6 + 4 + 3)).getComponent(PlayerActions.class);
    }
}


