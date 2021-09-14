package com.deco2800.game.components.statusEffects;

import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.statuseffects.StatusEffectEnum;
import com.deco2800.game.components.statuseffects.StatusEffectOperation;
import com.deco2800.game.components.tasks.WaitTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class StatusEffectTest {

    private StatusEffectOperation jumpBoost;
    private StatusEffectOperation speedBoost;
    private StatusEffectOperation stuckInTheMud;
    private Entity player;
    private CombatStatsComponent combatStatsComponentNotDead;
    private CombatStatsComponent combatStatsComponentIsDead;
    private PlayerActions playerActions;
    /* There are no intractable elements in this HashMap. */
    private final Map<ObstacleEntity, ObstacleEntity> mapInteractables = new HashMap<>();
    private ArrayList<String> statusEffectList = new ArrayList<>();


    @BeforeEach
    public void initialiseClasses() {
        playerActions = new PlayerActions();
        /* The health determines whether the unit is dead. 1 = alive, 0 = dead */
        combatStatsComponentIsDead = new CombatStatsComponent(0,0);
        combatStatsComponentNotDead = new CombatStatsComponent(1,0);
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

    @BeforeEach
    public void initialiseStatusEffectOperation() {
        /* Initialise the buff and debuff classes. Also redefining some methods for mocking purposes */
        speedBoost = new StatusEffectOperation(player, "Buff_Speed",  statusEffectList) {
            /* The speedChange() has been rewritten to change the stats in the playerActions class.
                 This is because the player Entity is a mock class, meaning that when we call functions that rely on
                 other classes, those classes will return null because it does not have them initialised,
                 i.e. player.getComponents(PlayerActions.class).getSpeed() will fail because
                 player.getComponent(PlayerActions.class) is null. The last JUnit test demonstrates this.
                 To solve this, we create a PlayerActions object and directly implement the changes on the object whenever
                 the player entity calls a function that affects PlayerActions. This is why when we call getSpeed(),
                 we change the PlayerActions object manually, the same way as it would be done in practice; only difference
                 is that in practice, the player Entity and PlayerActions object are explicitly linked, here they are not due to mocking.
                 */
            @Override
            public int speedChange(int type) {
                playerActions.alterSpeed(type * StatusEffectEnum.SPEED.getStatChange());
                System.err.println("In the redefine");
                //System.err.println(player.getComponent(CombatStatsComponent.class).isDead());

                if (player.getComponent(CombatStatsComponent.class).isDead()) {
                    System.err.println("Player is dead");
                    playerActions.alterSpeed(-StatusEffectEnum.SPEED.getStatChange());
                } else {
                    System.err.println("Player is not dead");
                    Timer t = new java.util.Timer();
                    t.schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here
                                    player.getComponent(PlayerActions.class).alterSpeed(-StatusEffectEnum.SPEED.getStatChange());
                                    // close the thread
                                    t.cancel();
                                }
                            },
                            StatusEffectEnum.SPEED.getStatDuration()
                    );
                }
                return StatusEffectEnum.SPEED.getStatChange();
            }
        };
        jumpBoost = new StatusEffectOperation(player, "Buff_Jump",  statusEffectList) {

        };
        stuckInTheMud = new StatusEffectOperation(player, "Debuff_Stuck",  statusEffectList) {
        };
    }

    @Test
    public void testSpeedChangeNotDead() {
        float expected;
        float result;
        int type = 1;

        // Tests stat change from enum (this is how the game will actually determine the stat changes)
        // Tests is not dead first
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentNotDead);

        expected = StatusEffectEnum.SPEED.getStatChange();
        result = speedBoost.speedChange(type);
        assertEquals(expected, result); /* Check the enum for SPEED */

        // Check the change in stat
        expected = 15f;
        result = player.getComponent(PlayerActions.class).getSpeed();
        verify(player).getComponent(PlayerActions.class); /* verify that this method is being called */
                                                          /* can't verify getSpeed() because playerActions is not a mock */
        assertEquals(expected, result); /* Assert the results */

        // Checks if the player's stats returns back to normal
        expected = 10;
        try {
            Thread.sleep(2000); // Increases the time the test takes to run
            result = player.getComponent(PlayerActions.class).getSpeed();
            assertNotEquals(expected, result); /* Tests if the statusEffect is still active. */

            Thread.sleep(StatusEffectEnum.SPEED.getStatDuration() - 2000);
            result = player.getComponent(PlayerActions.class).getSpeed();
            assertEquals(expected, result); /* Tests that the statusEffect is not active. */
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        System.err.println("After");
    }

    @Test
    public void testSpeedChangeIsDead() {
        float expected;
        float result;
        int type = 1;

        /** Tests the condition that the player is dead and got the power up */
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponentIsDead);
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        speedBoost.speedChange(type); /* Call the speed change */
        
        expected = 10f;
        result = player.getComponent(PlayerActions.class).getSpeed();

        assertEquals(expected, result); /* Check that the statusEffect goes away because the player is dead */
        assertNotEquals(15f, result); /* Checks that the statusEffect does not persist after death */
    }

    @Test
    public void testJumpBoost() {

    }

    /** The following tests below were done in order to confirm the correct usage of mocking and other miscellaneous
     tests to help with understanding. */

    @Test
    /* Preliminary test on the player.getComponent class. Making sure the methodology is valid */
    public void playerComponentReturn() {
        float expected = 10f;
        // Mock the first function.
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        // Mock the second function.
        //when(player.getComponent(PlayerActions.class).getSpeed()).thenReturn(10f);
        //System.err.println(player.getComponent(PlayerActions.class).getSpeed());
        //System.err.println(playerActions.getSpeed());
        assertEquals(expected, player.getComponent(PlayerActions.class).getSpeed());
    }

    @Test
    /* This test is to check that the @Before class is set up properly. */
    public void testArray() {
        String expected = "Buff_Jump";
        assertEquals(expected, statusEffectList.get(0));

        expected = "Buff_Time_Stop";
        assertEquals(expected, statusEffectList.get(1));

        expected = "Buff_Speed";
        assertEquals(expected, statusEffectList.get(2));

        expected = "Debuff_Bomb";
        assertEquals(expected, statusEffectList.get(3));

        expected = "Debuff_Speed";
        assertEquals(expected, statusEffectList.get(4));

        expected = "Debuff_Stuck";
        assertEquals(expected, statusEffectList.get(5));
    }

    @Test
    /* This test is to test that the mock class and when().thenReturn() statement works. */
    public void testMock() {
        when(player.toString()).thenReturn("hello");
        assertEquals("hello", player.toString());
        assertNotEquals("apple", player.toString());
    }

    /* Uncomment if you want to see how this works */
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
