package io.softwarechain.game
package engine.scene

import org.joml.Matrix4f

final class Projection(var projMatrix: Matrix4f) {
  import Projection.*

  def updateProjMatrix(width: Int, height: Int): Unit = {
    projMatrix = projMatrix.setPerspective(FOV, (width / height).toFloat, ZNear, ZFar)
  }
}

object Projection {
  val FOV: Float = Math.toRadians(60.0f).toFloat
  val ZFar: Float = 1000f
  val ZNear: Float = 0.01f

  def apply(width: Int, height: Int): Projection = {
    val projMatrix = new Matrix4f()
    val projection = new Projection(projMatrix)
    projection.updateProjMatrix(width, height)
    projection
  }
}
