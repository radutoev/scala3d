package io.softwarechain.game
package engine

import graph.Mesh

import org.lwjgl.opengl.GL11.{GL_TRIANGLES, glDrawArrays}
import org.lwjgl.opengl.GL30.glBindVertexArray

/**
 * Will hold the 3D scene and future models.
 */
class Scene {
  private var _meshMap: Map[String, Mesh] = Map.empty
  
  def addMesh(meshId: String, mesh: Mesh): Unit = {
    _meshMap = _meshMap + (meshId -> mesh)
  }
  
  def render(): Unit = _meshMap.foreach { case (_, mesh) =>
    glBindVertexArray(mesh.vaoId())
    glDrawArrays(GL_TRIANGLES, 0, mesh.numVertices)
  }
  
  def cleanup(): Unit = _meshMap.foreach { case (_, mesh) => 
    mesh.cleanup()
  }
}
