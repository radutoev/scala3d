package io.softwarechain.game
package engine.scene

import graph.{Mesh, Model}

import org.joml.{Vector3f, Vector4f}
import org.lwjgl.opengl.GL11.{GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
import org.lwjgl.opengl.GL30.glBindVertexArray

/**
 * Will hold the 3D scene and future models.
 */
class Scene(val projection: Projection) {
  var models: Map[String, Model] = Map.empty

  def addEntity(entity: Entity): Unit = {
    val modelId = entity.modelId
    models.get(modelId) match
      case Some(model) => models = models + (modelId -> model.addEntity(entity))
      case None => throw new RuntimeException(s"Could not find model [$modelId]")
  }
  
  def genEntity(id: String): Option[Entity] =
    models.find(_._2.entities.exists(_.id == id))
      .flatMap(_._2.entities.find(_.id == id))
    
  def updateEntity(id: String, displInc: Vector4f): Unit = {
    genEntity(id) match
      case Some(entity) => 
        val newEntity = entity.update(
          Point3D(
            entity.position.x + displInc.x,
            entity.position.y + displInc.y,
            entity.position.z + displInc.z),
          entity.scale + displInc.w
        )
        //replace the entity in the model
        models = models + (entity.modelId -> models(entity.modelId).updateEntity(newEntity))
      case None => throw new RuntimeException(s"Could not find entity [$id]")
  }
  
  def updateEntity(id: String, rotation: Float): Unit = {
    genEntity(id) match
      case Some(entity) => 
        val newEntity = entity.update(
          Rotation(1f, 1f, 1f, Math.toRadians(rotation).toFloat)
        )
        //replace the entity in the model
        models = models + (entity.modelId -> models(entity.modelId).updateEntity(newEntity))
      case None => throw new RuntimeException(s"Could not find entity [$id]")
  }
  
  def addModel(model: Model): Unit = { 
    models = models + (model.id -> model)
  }

//  def render(): Unit = _meshMap.foreach { case (_, mesh) =>
//    glBindVertexArray(mesh.vaoId())
//    /*
//     mode - we are drawing triangles
//     count - how  many elements to render
//     type - data type of the indices array
//     indices - offset to apply to the indices data to start rendering
//     */
//    glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
//  }

  def resize(width: Int, height: Int): Unit = {
    projection.updateProjMatrix(width, height)
  }

  def cleanup(): Unit = models.foreach { case (_, model) =>
    model.cleanup()
  }
}

object Scene:
  def apply(width: Int, height: Int): Scene = {
    new Scene(Projection(width, height))
  }
