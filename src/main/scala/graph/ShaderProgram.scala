package io.softwarechain.game
package graph

import engine.Resources

import org.lwjgl.opengl.GL20.*

final class ShaderProgram(val programId: Int) {
  def bind(): Unit = glUseProgram(programId)

  def unbind(): Unit = glUseProgram(0)

  def cleanup(): Unit = {
    unbind()
    if (programId ne 0) glDeleteProgram(programId)
  }

  // only for debugging.
  def validate(): Unit = {
    glValidateProgram(programId)
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) eq 0) throw new RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024))
  }
}

object ShaderProgram {
  def apply(shaderModuleData: List[ShaderModuleData]): ShaderProgram = {
    val programId = glCreateProgram()
    if (programId eq 0) throw new RuntimeException("Could not create Shader")
    val shaderModules: List[Int] = shaderModuleData.map(s => createShader(programId, Resources.readFile(s.shaderFile), s.shaderType))
    link(programId, shaderModules)
    new ShaderProgram(programId)
  }

  private def createShader(programId: Int, shaderCode: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    if (shaderId eq 0) throw new RuntimeException("Error creating shader. Type: " + shaderType)
    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)
    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) eq 0) {
      throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
    }
    glAttachShader(programId, shaderId)
    shaderId
  }

  private def link(programId: Int, shaderModules: List[Int]): Unit = {
    glLinkProgram(programId)
    if (glGetProgrami(programId, GL_LINK_STATUS) eq 0) throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
    // Once the shader program has been linked, the compiled vertex and fragment shaders can be freed up
    shaderModules.foreach(s => glDetachShader(programId, s))
    shaderModules.foreach(glDeleteShader)
  }
}

final case class ShaderModuleData(shaderFile: String, shaderType: Int)