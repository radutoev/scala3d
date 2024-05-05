package io.softwarechain.game
package app

import engine.Window
import graph.{Mesh, Render}
import io.softwarechain.game.engine.scene.Scene

/**
 * @deprecated
 */
class Triangle extends AppLogic {

  override def cleanup(): Unit = ()

  override def init(window: Window, scene: Scene, render: Render): Unit = {
    val positions = Array(
      0.0f, 0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f,
      0.5f, -0.5f, 0.0f,
    )
//    scene.addMesh("triangle", Mesh(positions, 3))
  }

  override def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()

  override def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()
}
