package io.softwarechain.game
package graph

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.{glGetUniformLocation, glUniformMatrix4fv}
import org.lwjgl.system.MemoryStack

import scala.util.Using

/**
 * @param programId Shader program identifier.
 * @param uniforms
 */
final class UniformsMap(programId: Int, var uniforms: Map[String, Int]) {
  def createUniform(uniformName: String): Unit = {
    val uniformLocation: Int = glGetUniformLocation(programId, uniformName)
    if (uniformLocation < 0) throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" + programId + "]")
    uniforms = uniforms + (uniformName -> uniformLocation)
  }

  def setUniform(uniformName: String, value: Matrix4f): Unit = {
    Using(MemoryStack.stackPush) { stack =>
      uniforms.get(uniformName) match
        case Some(location) => glUniformMatrix4fv(
          location.intValue(), 
          false, 
          value.get(stack.mallocFloat(16))
        )
        case None => throw new RuntimeException(s"Could not find uniform [$uniformName]")
    }
  }
}

object UniformsMap:
  def apply(programId: Int): UniformsMap = {
    new UniformsMap(programId, Map.empty)
  }