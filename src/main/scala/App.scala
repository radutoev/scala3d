package io.softwarechain.game

import app.Going3D
import engine.{Engine, WindowOptions}

@main def main(): Unit = {
  val app = new Going3D()
  val engine = new Engine("chapter-06", WindowOptions(compatibleProfile = false, fps = 0, height = 0, width = 0), app)
  engine.start()
}
