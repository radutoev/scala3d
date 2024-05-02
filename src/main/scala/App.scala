package io.softwarechain.game

import engine.{Engine, Scene, Window, WindowOptions}
import graph.Render

class MyApp extends AppLogic {

  override def cleanup(): Unit = ()

  override def init(window: Window, scene: Scene, render: Render): Unit = ()

  override def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()

  override def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit = ()
}

@main def main(): Unit = {
  val app = new MyApp()
  val engine = new Engine("chapter-02", WindowOptions(compatibleProfile = false, fps = 0, height = 0, width = 0), app)
  engine.start()
}
