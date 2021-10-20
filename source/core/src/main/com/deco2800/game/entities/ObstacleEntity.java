package com.deco2800.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.components.Component;


public class ObstacleEntity extends Entity implements Json.Serializable {
  public ObstacleDefinition definition;
  public int size;

  // ID Used only for mapping interactable entities
  public Integer interactableID = null;

  public ObstacleEntity() {}

  public ObstacleEntity(ObstacleDefinition definition, int size) {
    this.definition = definition;
    this.size = size;
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
  
  @Override
  public void write(Json json) {
    GridPoint2 position = this.getTilePosition();

    if (this.interactableID != null) {
      json.writeValue("interactableID", this.interactableID);
    }
    json.writeValue("def", this.definition.toString());
    json.writeValue("size", this.size);
    json.writeValue("x", position.x);
    json.writeValue("y", position.y);
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    this.definition = ObstacleDefinition.valueOf(jsonData.getString("def"));
    this.size = jsonData.getInt("size");

    if (jsonData.has("interactableID")) {
      this.interactableID = jsonData.getInt("interactableID");
    }

    Vector2 position = new Vector2();
    position.x = jsonData.getInt("x");
    position.y = jsonData.getInt("y");

    setPosition(position);
  }
}

