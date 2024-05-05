package io.softwarechain.game
package app
import engine.Window
import engine.scene.{Entity, Point3D, Scene}
import graph.{Mesh, Model, Render}

import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*

class Going3D extends AppLogic {
  private val _displayInc = new Vector4f()
  private var _rotation: Float = 0f

  override def cleanup(): Unit = ()

  override def init(window: Window, scene: Scene, render: Render): Unit = {
    val positions = Array[Float](
      // VO
      -0.5f, 0.5f, 0.5f, // V1
      -0.5f, -0.5f, 0.5f, // V2
      0.5f, -0.5f, 0.5f, // V3
      0.5f, 0.5f, 0.5f, // V4
      -0.5f, 0.5f, -0.5f, // V5
      0.5f, 0.5f, -0.5f, // V6
      -0.5f, -0.5f, -0.5f, // V7
      0.5f, -0.5f, -0.5f
    )

    val colors = Array[Float](
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f,
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f
    )
    val indices = Array[Int](
      // Front face
      0, 1, 3, 3, 1, 2, // Top Face
      4, 0, 3, 5, 4, 3, // Right face
      3, 2, 7, 5, 3, 7, // Left face
      6, 1, 0, 6, 0, 4, // Bottom face
      2, 1, 6, 2, 6, 7, // Back face
      7, 6, 4, 7, 4, 5
    )

    val cubeModelId = "cube-model"
    scene.addModel(Model(cubeModelId, List(new Mesh(positions, colors, indices))))
    scene.addEntity(Entity("cube-entity", cubeModelId, Point3D(0, 0, -2)))
  }

  override def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit = {
    _displayInc.zero()

    if (window.isKeyPressed(GLFW_KEY_UP)) _displayInc.y = 1
    else if (window.isKeyPressed(GLFW_KEY_DOWN)) _displayInc.y = -1
    if (window.isKeyPressed(GLFW_KEY_LEFT)) _displayInc.x = -1
    else if (window.isKeyPressed(GLFW_KEY_RIGHT)) _displayInc.x = 1
    if (window.isKeyPressed(GLFW_KEY_A)) _displayInc.z = -1
    else if (window.isKeyPressed(GLFW_KEY_Q)) _displayInc.z = 1
    if (window.isKeyPressed(GLFW_KEY_Z)) _displayInc.w = -1
    else if (window.isKeyPressed(GLFW_KEY_X)) _displayInc.w = 1

    _displayInc.mul(diffTimeMillis / 1000.0f)

    scene.updateEntity("cube-entity", _displayInc)
  }

  override def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit = {
    _rotation = _rotation + 1.5f
    if (_rotation > 360) _rotation = 0
    scene.updateEntity("cube-entity", _rotation)
  }
}
