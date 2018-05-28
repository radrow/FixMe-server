package controllers

import scala.util.Random

object Evacuation {

  var isNow = false
  var id = 0

  def isEvacuation: Option[Int] = {
    if (isNow)
      Option(id)
    else
      None
  }

}
