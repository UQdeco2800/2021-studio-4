package com.deco2800.game.components.statusEffects;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.statuseffects.StatusEffectTargetComponent;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
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
    private StatusEffectTargetComponent SETC;
    private GameTime gameTime;
    private PlayerActions playerActions;

    private float expected;
    private float result;

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
    public void initialiseClasses() {
        /* Mocking classes */
        player = Mockito.mock(Entity.class);
        gameTime = Mockito.mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);

        /* Initialise other classes */
        playerActions = new PlayerActions();
        combatStatsComponent = new CombatStatsComponent(1, 0);
        SETC = new StatusEffectTargetComponent();
        SETC.setEntity(player);
    }

    @Test
    public void testApplySpeedBuff() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add speed boost */
        SETC.applyStatusEffect(StatusEffect.FAST);
        /* We expect the getComponent(PlayerActions.class) is called twice */
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.FAST, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplyJumpBuff() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        /* Add jump boost */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        verify(player, times(1)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.JUMP, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplyTimeStop() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
        SETC.applyStatusEffect(StatusEffect.TIME_STOP);
        verify(player, times(0)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.TIME_STOP, SETC.getCurrentStatusEffect());
    }

    @Test
    public void testApplySpeedDebuff() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
        SETC.applyStatusEffect(StatusEffect.SLOW);
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertStatusEffects(StatusEffect.SLOW, SETC.getCurrentStatusEffect());
    }

 //   @Test
   // public void testApplyStuckInMud() {
   //     when(gameTime.getTime()).thenReturn(0L);
   //     when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
   //     when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Add time stop buff */
   //     SETC.applyStatusEffect(StatusEffect.STUCK);
   //     verify(player, times(2)).getComponent(PlayerActions.class);
   //     assertStatusEffects(StatusEffect.STUCK, SETC.getCurrentStatusEffect());
   // }

    @Test
    public void testJumpBuffStatChange() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* apply jump buff */
        SETC.applyStatusEffect(StatusEffect.JUMP);
        result = playerActions.getJumpHeight();
        expected = 600;
        verify(player).getComponent(PlayerActions.class);
        assertTestCase(expected, result);
    }

   /* @Test
    public void testStuckInTheMudStatChange() {
        PlayerActions playerStuckInTheMud = new PlayerActions();
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerStuckInTheMud);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* apply jump buff */
      //  SETC.applyStatusEffect(StatusEffect.STUCK);
      //  result = playerStuckInTheMud.getSpeed();
      //  expected = 0f;
      //  verify(player, times(2)).getComponent(PlayerActions.class);
    //    assertTestCase(expected, result);
  //  }

    @Test
    public void testSpeedDebuffStatChange() {
        when(gameTime.getTime()).thenReturn(0L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* apply jump buff */
        SETC.applyStatusEffect(StatusEffect.SLOW);
        result = playerActions.getSpeed();
        expected = 5;
        verify(player, times(2)).getComponent(PlayerActions.class);
        assertTestCase(expected, result);
    }

   /* @Test
    public void testUpdateStatusEffect() {
        when(gameTime.getTime()).thenReturn(0L);
        when(gameTime.getTimeSince(0L)).thenReturn(6000L);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        /* Test speed buff */
      //  SETC.applyStatusEffect(StatusEffect.FAST);
        // Check that the status effect has been added.
       // assertStatusEffects(StatusEffect.FAST, SETC.getCurrentStatusEffect());
       // Remove the status effect.
       // SETC.update();
       // assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test jump buff */
      //  SETC.applyStatusEffect(StatusEffect.JUMP);
        // Check that the status effect has been added.
      //  assertStatusEffects(StatusEffect.JUMP, SETC.getCurrentStatusEffect());
        // Remove the status effect.
      //  SETC.update();
      //  assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test time stop */
       // SETC.applyStatusEffect(StatusEffect.TIME_STOP);
        // Check that the status effect has been added.
      //  assertStatusEffects(StatusEffect.TIME_STOP, SETC.getCurrentStatusEffect());
        // Remove the status effect.
       // SETC.update();
       /* assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test speed debuff*/
       // SETC.applyStatusEffect(StatusEffect.SLOW);
        // Check that the status effect has been added.
      //  assertStatusEffects(StatusEffect.SLOW, SETC.getCurrentStatusEffect());
        // Remove the status effect.
      //  SETC.update();
      //  assertStatusEffects(null, SETC.getCurrentStatusEffect());

        /* Test stuck in the mud debuff */
       // SETC.applyStatusEffect(StatusEffect.STUCK);
        // Check that the status effect has been added.
       // assertStatusEffects(StatusEffect.STUCK, SETC.getCurrentStatusEffect());
        // Remove the status effect.
       // SETC.update();
       // assertStatusEffects(null, SETC.getCurrentStatusEffect());
    //}

    @Test
    public void testApplyStatusEffectBehaviour() {
        when(gameTime.getTime()).thenReturn(0L);
        when(gameTime.getTimeSince(0L)).thenReturn(6000L);
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
