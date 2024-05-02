package io.softwarechain.game

import engine.{Scene, Window}
import graph.Render

trait AppLogic {
  def cleanup(): Unit

  def init(window: Window, scene: Scene, render: Render): Unit

  def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit

  def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit
}
