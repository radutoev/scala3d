package io.softwarechain.game
package engine.scene

import org.joml.{Matrix4f, Quaternionf, Vector3f}

final case class Entity(id: String,
                        modelId: String,
                        modelMatrix: Matrix4f,
                        position: Vector3f,
                        rotation: Quaternionf,
                        scale: Float) {

  def update(toPosition: Point3D, scale: Float): Entity = {
    val newPosition = new Vector3f(toPosition.x, toPosition.y, toPosition.z)
    copy(
      modelMatrix = modelMatrix.translationRotateScale(newPosition, rotation, scale),
      position = newPosition,
      rotation = rotation,
      scale = scale
    )
  }

  def update(withRotation: Rotation): Entity = {
    val newRotation = rotation.fromAxisAngleRad(withRotation.x, withRotation.y, withRotation.z, withRotation.angle)
    copy(
      modelMatrix = modelMatrix.translationRotateScale(position, newRotation, scale),
      rotation = newRotation,
    )

  }
}


object Entity:
  def apply(id: String, modelId: String, position: Point3D): Entity =
    new Entity(id, modelId, new Matrix4f(), position.asVector3f, new Quaternionf(), 1)


final case class Point3D(x: Float, y: Float, z: Float) { 
  def asVector3f: Vector3f = new Vector3f(x, y, z)
}

final case class Rotation(x: Float, y: Float, z: Float, angle: Float)