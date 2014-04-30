package eu.nevans.rakka.actors

import akka.actor.Actor
import akka.actor.ActorLogging



class Validator extends Actor with ActorLogging {

  def receive = {
    case Validator.ValidationTask(task, int) => 
      val result = task(int)
      result match {
        case true => sender ! Validator.Success(int)
        case false => sender ! Validator.Failure(int)
      }
  }
 
}

object Validator {
  
  case class ValidationTask(task: Int => Boolean, int: Int)
  case class Success(notPrime: Int)
  case class Failure(prime: Int)
  
}