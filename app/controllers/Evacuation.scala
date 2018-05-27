package controllers

import scala.util.Random

object Evacuation {

  val random: Random.type = Random
  var isNow = false
  var id = 0

  def isEvacuation: Option[Int] = {
    if (random.nextInt(5) == 1) {
      isNow = !isNow
      if (isNow)
        id += 1
    }

    if (isNow)
      Option(id)
    else
      None
  }

}
