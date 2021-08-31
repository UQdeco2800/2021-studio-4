package com.deco2800.game.components.npc;
// To play sound (not music)
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
//
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.math.Vector2;

import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.utils.math.Vector2Utils;

import static java.lang.Math.abs;

public class TheVoidController extends Component {
    private static final Vector2 ACCELERATION = new Vector2(8f, 0f);

    private PhysicsComponent physicsComponent;
    private AnimationRenderComponent animator;
    private Body body;
    private Entity player;
    //
    MusicServiceDirectory dict = new  MusicServiceDirectory();
    MusicService musicService = new MusicService(dict.void_noise);

    public TheVoidController(Entity target){
        player = target;
    }

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setGravityScale(0);

        entity.getEvents().addListener("TheVoidAnimate", this::theVoidAnimate);
        entity.getEvents().addListener("TheVoidMove", this::theVoidMove);
        entity.getEvents().addListener("UpdateSound", this::updateSound);

        this.body = physicsComponent.getBody();
    }


     void theVoidAnimate(){
        animator.startAnimation("void");
    }

     void theVoidMove(){
        this.body.applyForceToCenter(Vector2Utils.RIGHT.cpy().scl(ACCELERATION), true);
    }

     private float getPlayerDistance(){
        float distance_x;
        float void_length = this.entity.getScale().x;
        Vector2 void_pos = this.entity.getPosition();
        distance_x = player.getPosition().sub(void_pos).x - void_length;
        return distance_x;
    }

     void updateSound(){
         float distance_from_player = getPlayerDistance();
         float min = 0;
         float max = 1;
         if (distance_from_player < (float)(-8)){
             musicService.stopMusic(); //It should be fine for now. Later when the void team could detect void-player
             // collision, we stop the music at that point.
         } else if (distance_from_player > (float)0.01) {
             float change1 = abs(1 - distance_from_player);
             if (change1 > (float)1) {
                 musicService.changeVolume((float)0.2);
             } else {
                 musicService.changeVolume((float)0.4);
             }
         }
         else if (distance_from_player < (float)0.99) {
             float change2 = abs(1 - distance_from_player);
             if (change2 > (float)1) {
                 musicService.changeVolume((float)0.9);
             } else {
                 musicService.changeVolume((float)0.6);
             }
         }
        /*if (distance_from_player > (float)0.01) {
             float change1 = 1 - distance_from_player;
             musicService.changeVolume(change1);
         }
         else if (distance_from_player < (float)0.99) {
             float change2 = 1 - distance_from_player;
             musicService.changeVolume(change2);
         }
         else {
             //musicService.changeVolume((float)0.5);
         }*/

    }



}
