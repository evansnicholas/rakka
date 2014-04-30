package eu.nevans.rakka.actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorLogging

class ValidationManager extends Actor with ActorLogging {

  val tasks = (1 until 5000) map { int => Validator.ValidationTask(ValidationManager.isPrime, int) }
  
  var responses: Int = 0
  var primes: List[Int] = List.empty[Int]
  
  override def preStart(): Unit = {

    // create the validations actors and send them their tasks
    tasks foreach { task =>
      val validator = context.actorOf(Props[Validator], s"Validator${task.int}")  
      log.info(s"Checking if ${task.int} is prime.")
      // tell it to perform the greeting
      validator ! task
    }
  }

  def receive = {
    // when the greeter is done, stop this actor and with it the application
    
    case Validator.Success(notPrime) => {
      log.debug(s"$notPrime is not prime.")
      responses += 1
      if (responses == tasks.size){
        log.info(s"Found the following primes: \n $primes")
        context.stop(self)
      }
      else Unit
    }
    case Validator.Failure(prime) =>
      log.debug(s"Found prime $prime")
      primes = primes :+ prime
      responses += 1
      if (responses == tasks.size){
        log.info(s"Found the following primes: \n $primes")
        context.stop(self)
      }
      else Unit
  }
  
}

object ValidationManager {
  
  def isPrime(int: Int): Boolean = {
    (2 until int) exists { factor => int%factor == 0 }
  }
  
}