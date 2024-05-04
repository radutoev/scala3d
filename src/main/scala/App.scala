package io.softwarechain.game

import app.Quad
import engine.{Engine, WindowOptions}

@main def main(): Unit = {
  val app = new Quad()
  val engine = new Engine("chapter-03", WindowOptions(compatibleProfile = false, fps = 0, height = 0, width = 0), app)
  engine.start()
}
