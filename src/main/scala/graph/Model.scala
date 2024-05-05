package io.softwarechain.game
package graph

import engine.scene.Entity

/**
 * A model is an structure which glues together vertices, colors, textures and materials.
 * A model may be composed of several meshes and can be used by several game entities.
 * A game entity represents a player and enemy, and obstacle, anything that is part of the 3D scene.
 * We will assume that an entity always is related to a model (although you can have entities that are not rendered and therefore cannot have a model).
 * An entity has specific data such a position, which we need to use when rendering.
 *
 * Note:  If you are going to create a full engine, you may want to store those relationships in somewhere else (not in the model), however, for simplicity we will store those links in the Model class.
 *
 * In order to represent any model in a 3D scene we need to provide support for some basic operations to act upon any model:
 *    1. Translation:  Move an object by some amount on any of the three axes
 *       2. Rotation: Rotate an object by some amount of degrees around any of the three axes.
 *       3. Adjust the size of an object.
 *
 * The way we are going to achieve that is by multiplying our coordinates by a set of matrices (one for translation, one for rotation and one for scaling). 
 * Those three matrices will be combined into a single matrix called world matrix and passed as a uniform to our vertex shader.
 * It is named world matrix, because we are transforming from model coordinates to world coordinates.
 * World Matrix = TranslationMatrix x RotationMatrix x ScaleMatrix (order is important)
 */
final case class Model private(id: String, entities: List[Entity], meshes: List[Mesh]) {
  def addEntity(entity: Entity): Model = copy(entities = this.entities :+ entity)

  def updateEntity(entity: Entity): Model = {
    val index = entities.indexWhere(_.id == entity.id)
    if (index == -1) addEntity(entity)
    else copy(entities = entities.updated(index, entity))
  }

  def cleanup(): Unit = {
    meshes.foreach(_.cleanup())
  }
}

object Model {
  def apply(id: String, meshes: List[Mesh]): Model =
    new Model(id, List.empty, meshes)
}
