package eu.nevans.rakka

import eu.nevans.rakka.actors.ValidationManager

object Main {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[ValidationManager].getName))
  }
  
}