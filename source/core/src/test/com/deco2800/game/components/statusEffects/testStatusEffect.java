package com.deco2800.game.components.statusEffects;

import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.components.npc.StatusEffectsController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.statuseffects.StatusEffectEnum;
import com.deco2800.game.components.statuseffects.StatusEffectOperation;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class testStatusEffect {
    StatusEffectOperation jumpBoost;
    StatusEffectOperation speedBoost;
    StatusEffectOperation stuckInTheMud;
    /* There are no intractable elements in this HashMap. */
    private Map<ObstacleEntity, ObstacleEntity> mapInteractables = new HashMap<>();
    private ArrayList<String> statusEffectList;
//    private String[] statusEffects = {"Buff_Jump", "Buff_Time_Stop", "Buff_Speed", "Debuff_Bomb"
//            , "Debuff_Speed", "Debuff_Stuck"};
    private Entity player;
    @Before
    public void initialise() {
        MockitoAnnotations.openMocks(this);

        statusEffectList = new ArrayList<>();
        statusEffectList.add(0,"Buff_Jump");
        statusEffectList.add(1,"Buff_Time_Stop");
        statusEffectList.add(2,"Buff_Speed");
        statusEffectList.add(3,"Debuff_Bomb");
        statusEffectList.add(4,"Debuff_Speed");
        statusEffectList.add(5,"Debuff_Stuck");

        player = PlayerFactory.createPlayer(mapInteractables);
        speedBoost = new StatusEffectOperation(player, "Buff_Speed",  statusEffectList);
        jumpBoost = new StatusEffectOperation(player, "Buff_Jump",  statusEffectList);
        stuckInTheMud = new StatusEffectOperation(player, "Debuff_Stuck",  statusEffectList);
    }


    // Mock classes.
    @Mock
    //StatusEffectOperation changeStat;
    LevelGameArea leveGameArea;
    NPCFactory NPCfactory;
    StatusEffectsController sec;
    Entity player1;



    @Test
    public void testBuffNewValues() {
        /* Test Buff_Speed */
        //when(player1.getComponent(PlayerActions.class).getSpeed()).thenReturn(300f);

//        float expected = StatusEffectEnum.SPEED.getStatChange() +
//                player1.getComponent(PlayerActions.class).getSpeed();

        StatusEffectOperation sB = new StatusEffectOperation(player, "Buff_Speed", statusEffectList);
        float expected  = 500f;
        System.err.println("Before");
        //float result = speedBoost.speedChange(1);
        float result = sB.speedChange(1);
        System.err.println("After");

        System.err.println(result);

        assertEquals(expected, result);
    }

    @Test
    public void testMock() {
        when(sec.getPlayerDistance()).thenReturn(300f);
        float expected = 300f;
        assertEquals(expected, sec.getPlayerDistance());
    }
}
