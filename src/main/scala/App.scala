package io.softwarechain.game

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

import scala.util.Using

private class App(val window: Long = App.init()):
  def run(): Unit = {
    println("Hello LWJGL " + Version.getVersion + "!")

    loop()

    // free the window callbacks and destroy the window
    glfwDestroyWindow(window)

    //terminate glfw
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def loop(): Unit = {
    // set the clear color
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

    while(!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer or clear screen.

      glfwSwapBuffers(window) // swap the color buffers

      // poll the window events. the key callback will only be invoked during this call.
      glfwPollEvents()
    }
  }


end App

private object App:
  private def init() = {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err)

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalArgumentException("Unable to initialize GLFW")
    }

    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default.
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    // Create the window
    val window = glfwCreateWindow(300, 300, "Hello, World!", NULL, NULL)
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window")
    }

    // Setup key callback. Called everytime a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) => {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop.
      }
    })

    // Get the thread stack and push a new frame
    Using(stackPush()) { stack =>
      val pWidth = stack.mallocInt(1) //int*
      val pHeight = stack.mallocInt(2) //int*

      // get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight)

      // get the resolution of the primary monitor
      val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

      // center the window
      glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2)
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)

    // Enable v-sync
    glfwSwapInterval(1)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    // Make the window visible
    glfwShowWindow(window);

    window
  }
end App

@main def main(): Unit = new App().run()
