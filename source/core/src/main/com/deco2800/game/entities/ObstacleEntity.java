package com.deco2800.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.components.Component;


public class ObstacleEntity extends Entity {
  private final ObstacleDefinition definition;
  private final int size;

  public ObstacleEntity(ObstacleDefinition definition, int size) {
    this.definition = definition;
    this.size = size;
  }

  public String serialise() {
    GridPoint2 position = this.getTilePosition();
    String serialised = String.format("%s:%s:%s:%s\n",this.definition,this.size, position.x,position.y);
    return serialised;
  }

  @Override
  public ObstacleEntity addComponent(Component component) {
    return (ObstacleEntity)super.addComponent(component);
  }

  /**
   * Returns the definition of the obstacle.
   * @return Obstacle definition
   */
  public ObstacleDefinition getDefinition() {
    return this.definition;
  }
}

