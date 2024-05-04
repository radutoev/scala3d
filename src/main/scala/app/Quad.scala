package io.softwarechain.game
package app
import engine.{Scene, Window}
import graph.{Mesh, Render}

class Quad extends AppLogic {
  override def cleanup(): Unit = ()

  override def init(window: Window, scene: Scene, render: Render): Unit = {
    val positions = Array[Float](
      -0.5f, 0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f,
      0.5f, -0.5f, 0.0f,
      0.5f, 0.5f, 0.0f
    )
    val colors = Array[Float](
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f
    )
    val indices = Array[Int](0, 1, 3, 3, 1, 2)
    val mesh = new Mesh(positions, colors, indices)
    scene.addMesh("quad", mesh)
  }

  override def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()

  override def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()
}
