package io.softwarechain.game
package graph

import engine.scene.Scene

import org.lwjgl.opengl.GL11.{GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
import org.lwjgl.opengl.GL20.{GL_FRAGMENT_SHADER, GL_VERTEX_SHADER}
import org.lwjgl.opengl.GL30.glBindVertexArray

final class SceneRender private(shaderProgram: ShaderProgram, uniformsMap: UniformsMap) {
  def render(scene: Scene): Unit = {
    shaderProgram.bind()
    uniformsMap.setUniform("projectionMatrix", scene.projection.projMatrix)
    scene.models.foreach { case (_, model) =>
      model.meshes.foreach { mesh =>
        glBindVertexArray(mesh.vaoId())
        model.entities.foreach(entity => {
          uniformsMap.setUniform("modelMatrix", entity.modelMatrix)
          glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
        })
      }
    }
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
    uniformsMap.createUniform("modelMatrix")

    new SceneRender(shaderProgram, uniformsMap)
  }
}
