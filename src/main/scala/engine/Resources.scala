package io.softwarechain.game
package engine

import scala.io.Source
import scala.util.{Failure, Success, Using}

object Resources {
  def readFile(filePath: String): String = {
    Using(Source.fromResource(filePath)) { resource =>
      resource.map(_.toByte).toArray
    } match
      case Failure(exc) => throw new RuntimeException("Error reading file [" + filePath + "]", exc)
      case Success(value) => new String(value)
  }
}
