package io.github.silvaren.quotepersistence

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object SequencedResults {

  def delayedFuture(duration: Int) = Future{
    Thread.sleep(duration)
    println(s"Finishing $duration!")
    duration
  }

  val delaySequence = Seq(
    1000,
    900,
    800,
    700,
    600,
    500,
    400,
    300,
    200,
    100)

  def main(args: Array[String]) {
    val sequencedResults = Future.sequence(delaySequence.map(delay => delayedFuture(delay)))

    // Usually avoid Await because it is blocking, but we will use it
    // to prevent termination of the app before the results are in
    println("The final result: " + Await.result(sequencedResults,  Duration.Inf))
  }
}
