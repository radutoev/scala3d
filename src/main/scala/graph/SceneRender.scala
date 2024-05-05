package io.softwarechain.game
package graph

import engine.scene.Scene

import org.lwjgl.opengl.GL20.{GL_FRAGMENT_SHADER, GL_VERTEX_SHADER}
import org.lwjgl.opengl.GL30.glBindVertexArray

final class SceneRender private(shaderProgram: ShaderProgram, uniformsMap: UniformsMap) {
  def render(scene: Scene): Unit = {
    shaderProgram.bind()
    uniformsMap.setUniform("projectionMatrix", scene.projection.projMatrix)
    scene.render()
    glBindVertexArray(0)
    shaderProgram.unbind()
  }

  def cleanup(): Unit = shaderProgram.cleanup()
}

object SceneRender {
  def apply(): SceneRender = {
    val shaderProgram = ShaderProgram(List(
      ShaderModuleData("shaders/scene.vert", GL_VERTEX_SHADER),
      ShaderModuleData("shaders/scene.frag", GL_FRAGMENT_SHADER)
    ))
    val uniformsMap = UniformsMap(shaderProgram.programId)
    uniformsMap.createUniform("projectionMatrix")

    new SceneRender(shaderProgram, uniformsMap)
  }
}
