package io.softwarechain.game

import app.Triangle
import engine.{Engine, WindowOptions}

@main def main(): Unit = {
  val app = new Triangle()
  val engine = new Engine("chapter-02", WindowOptions(compatibleProfile = false, fps = 0, height = 0, width = 0), app)
  engine.start()
}
