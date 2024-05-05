package io.softwarechain.game
package graph

import engine.Window
import io.softwarechain.game.engine.scene.Scene

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

/**
 * Kind of a placeholder that just clears the screen
 */
class Render(sceneRender: SceneRender):
  def render(window: Window, scene: Scene): Unit = {
    GL.createCapabilities
    glEnable(GL_DEPTH_TEST)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    //we set the rendering area to the width / height dimensions.
    glViewport(0, 0, window.width(), window.height())
    sceneRender.render(scene)
  }

  def cleanup(): Unit = {
    sceneRender.cleanup()
  }
end Render

object Render:
  def apply(): Render = {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()
    new Render(SceneRender.apply())
  }