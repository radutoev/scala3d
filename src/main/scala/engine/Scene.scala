package io.softwarechain.game
package engine

import graph.Mesh

import org.lwjgl.opengl.GL11.{GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
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
    /*
     mode - we are drawing triangles
     count - how  many elements to render
     type - data type of the indices array
     indices - offset to apply to the indices data to start rendering
     */
    glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
  }
  
  def cleanup(): Unit = _meshMap.foreach { case (_, mesh) => 
    mesh.cleanup()
  }
}
