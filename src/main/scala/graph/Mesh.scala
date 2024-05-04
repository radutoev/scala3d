package io.softwarechain.game
package graph

import org.lwjgl.opengl.{GL11, GL30}
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack

import java.nio.FloatBuffer
import scala.util.Using

/**
 * defines the structure of that data and loads it in the GPU 
 * Vertex Array Objects (VAOs) and Vertex Buffer Object (VBOs)
 * 
 * A VBO is just a memory buffer stored in the graphics card memory that stores vertices. 
 * This is where we will transfer our array of floats that model a triangle. 
 * OpenGL does not know anything about our data structure. In fact it can hold not just coordinates but other information, such as textures, color, etc. 
 * A Vertex Array Objects (VAOs) is an object that contains one or more VBOs which are usually called attribute lists. Each attribute list can hold one type of data: position, color, texture, etc.
 *
 * @param positions vertex positions
 * @param colors colors for vertices
 * @param indices vertex position
 */
final class Mesh(positions: Array[Float], colors: Array[Float], indices: Array[Int]) {
  lazy val numVertices: Int = indices.length

  private var _vaoId: Int = _
  private var _vboIds: List[Int] = _

  Using(MemoryStack.stackPush) { stack =>
    _vaoId = glGenVertexArrays
    glBindVertexArray(_vaoId)

    // Positions VBO
    val posVboId = glGenBuffers()
    val positionsBuffer: FloatBuffer = stack.callocFloat(positions.length)
    positionsBuffer.put(0, positions)
    glBindBuffer(GL_ARRAY_BUFFER, posVboId)
    glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    //color VBO
    val colorVboId = glGenBuffers()
    val colorsBuffer = stack.callocFloat(colors.length)
    colorsBuffer.put(0, colors)
    glBindBuffer(GL_ARRAY_BUFFER, colorVboId)
    glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(1)
    glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0)

    //index VBO
    val indexVboId = glGenBuffers()
    val indicesBuffer = stack.callocInt(indices.length)
    indicesBuffer.put(0, indices)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

    _vboIds = List(posVboId, colorVboId, indexVboId)
  }
  
  def cleanup(): Unit = {
    _vboIds.foreach(glDeleteBuffers)
    glDeleteVertexArrays(_vaoId)
  }
  
  def vaoId(): Int = _vaoId
}
