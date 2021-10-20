package com.deco2800.game.components.statuseffects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;


public class StatusEffectUIComponent extends Component {
  private Entity uiEntity = null;
  private StatusEffect currentStatusEffect = null;
  private Long currentStatusEffectStartTime = null;

  private void setStatusEffect(StatusEffect statusEffect) {
    if (uiEntity != null) { return; }

    currentStatusEffect = statusEffect;
    currentStatusEffectStartTime = ServiceLocator.getTimeSource().getTime();

    TextureAtlas atlas = ServiceLocator.getResourceService()
      .getAsset(statusEffect.getUiAnimationAtlas(), TextureAtlas.class);

    AnimationRenderComponent animator =
      new AnimationRenderComponent(atlas);
    animator.addAnimation(statusEffect.getUiAnimationName(), 0.1f, Animation.PlayMode.NORMAL);

    uiEntity = new Entity().addComponent(animator);
    uiEntity.setScale(0.75f,0.75f);

    ServiceLocator.getEntityService().register(uiEntity);

    animator.startAnimation(statusEffect.getUiAnimationName());
  }

  @Override
  public void create() {
    entity.getEvents().addListener("StatusEffectTrigger", this::setStatusEffect);
    super.create();
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getEntityService().unregister(uiEntity);
    uiEntity.dispose();
  }

  @Override
  public void update() {
    if (uiEntity != null) {
      Vector2 uiPos = this.getEntity().getPosition().cpy();
      uiPos.add((float) 0.1, 1);

      uiEntity.setPosition(uiPos);

      if (ServiceLocator.getTimeSource().getTimeSince(currentStatusEffectStartTime) >= currentStatusEffect.getDuration()* 1000L) {
        currentStatusEffect = null;
        currentStatusEffectStartTime = null;
        ServiceLocator.getEntityService().unregister(uiEntity);
        uiEntity.dispose();
        uiEntity = null;
      }
    }
  }
}
