package io.softwarechain.game
package engine

import graph.Render

final class Engine(title: String, options: WindowOptions, appLogic: AppLogic) {
  val window = new Window(title, options, () => {
    resize()
  })
  //  val render = new Render()
  val scene = new Scene()
  val render = new Render
  private var _running = true

  appLogic.init(window, scene, null)

  def start(): Unit = {
    _running = true
    run()
  }

  def stop(): Unit = {
    _running = false
  }

  private def run(): Unit = {
    var initialTime = System.currentTimeMillis
    val timeU = 1000.0f / options.ups // max elapsed time between updates => game state
    val timeR = if (options.fps > 0) 1000.0f / options.fps else 0 // max elapsed time between render calls 
    var deltaUpdate = 0.0f
    var deltaFps = 0.0f

    var updateTime = initialTime;
    while (_running && !window.windowShouldClose()) {
      window.pollEvents()

      val now = System.currentTimeMillis()
      deltaUpdate = deltaUpdate + (now - initialTime) / timeU
      deltaFps = deltaFps + (now - initialTime) / timeR

      if (options.fps <= 0 || deltaFps >= 1) {
        // If we have passed the maximum elapsed time for render (or relay in v-sync), we process user input
        appLogic.input(window, scene, now - initialTime)
      }

      if (deltaUpdate >= 1) { // we update the state of the game. 
        val diffTimeMillis = now - updateTime
        appLogic.update(window, scene, diffTimeMillis)
        updateTime = now
        deltaUpdate = deltaUpdate - 1
      }

      if (options.fps <= 0 || deltaFps >= 1) { // we need to render. 
        render.render(window, scene)
        deltaFps = deltaFps - 1
        window.update()
      }

      initialTime = now
    }

    cleanup()
  }

  private def resize(): Unit = {

  }

  private def cleanup(): Unit = {
    appLogic.cleanup()
    // TODO Render.cleanup??
    //    scene.cleanup // TODO Scene cleanup
    window.cleanup()
  }

}
