package io.softwarechain.game
package graph

import engine.{Scene, Window}

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

/**
 * Kind of a placeholder that just clears the screen
 */
class Render:
  // This line is critical for LWJGL's interoperation with GLFW's
  // OpenGL context, or any context that is managed externally.
  // LWJGL detects the context that is current in the current thread,
  // creates the GLCapabilities instance and makes the OpenGL
  // bindings available for use.
  GL.createCapabilities()

  def render(window: Window, scene: Scene): Unit = glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
end Render