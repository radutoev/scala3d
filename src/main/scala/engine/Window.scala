package io.softwarechain.game
package engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallbackI
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL

import java.util.concurrent.Callable

final class Window(title: String, opts: WindowOptions, resizeFn: Callable[Unit]) {
  private var _width = opts.width
  private var _height = opts.height

  if (!glfwInit()) {
    throw new IllegalStateException("Unable to initialize GLFW")
  }

  glfwDefaultWindowHints()
  glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
  glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)

  if (opts.compatibleProfile) {
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
  } else {
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
  }

  if (opts.width > 0 && opts.height > 0) {
    _width = opts.width
    _height = opts.height
  } else {
    glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)
    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    _width = vidMode.width()
    _height = vidMode.height()
  }

  val handle: Long = glfwCreateWindow(_width, _height, title, NULL, NULL)

  glfwSetFramebufferSizeCallback(handle, (_, w, h) => resized(w, h))
  glfwSetErrorCallback((errorCode: Int, msgPtr: Long) => println(s"Error code [$errorCode], msg [${MemoryUtil.memUTF8(msgPtr)}]"))
  glfwSetKeyCallback(handle, (_, key, _, action, _) => keyCallBack(key, action))
  glfwMakeContextCurrent(handle)

  if (opts.fps > 0) {
    glfwSwapInterval(0) // this disables v-sync
  } else {
    glfwSwapInterval(1)
  }

  glfwShowWindow(handle)

  private val _arrWidth: Array[Int] = Array.ofDim[Int](1)
  private val _arrHeight: Array[Int] = Array.ofDim[Int](1)
  glfwGetFramebufferSize(handle, _arrWidth, _arrHeight)
  _width = _arrWidth.head
  _height = _arrHeight.head

  protected def resized(width: Int, height: Int): Unit = {
    _width = width
    _height = height

    try {
      resizeFn.call()
    } catch
      case e: Exception => println(s"Exception + ${e.toString}") // TODO Setup logging.
  }

  private def keyCallBack(key: Int, action: Int) = {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
      glfwSetWindowShouldClose(this.handle, true); // We will detect this in the rendering loop
    }
  }

  def cleanup(): Unit = {
    glfwDestroyWindow(handle)
    glfwTerminate()
    val callback = glfwSetErrorCallback(null)
    if (callback != null) callback.free()
  }

  def isKeyPressed(keyCode: Int) = glfwGetKey(this.handle, keyCode) == GLFW_PRESS

  def pollEvents(): Unit = glfwPollEvents()

  def update(): Unit = glfwSwapBuffers(handle)

  def windowShouldClose(): Boolean = glfwWindowShouldClose(handle)
}

/**
 * @param compatibleProfile This controls wether we want to use old functions from previous versions (deprecated functions) or not.
 * @param fps               Defines the target frames per second (FPS). If it has a value equal os less than zero it will mean that we do not want to set up a target but either use monitor refresh that as target FPS. In order to do so, we will use v-sync (that is the number of screen updates to wait from the time
 * @param height            Desired window height.
 * @param width             Desired window width
 * @param ups               Defines the target number of updates per second (initialized to a default value).
 */
final case class WindowOptions(compatibleProfile: Boolean, fps: Int, height: Int, width: Int, ups: Int = 30)
