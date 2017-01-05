package io.github.silvaren.quotepersistence

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object SequencedExecution {

  def delayedFuture(duration: Int) = Future{
    Thread.sleep(duration)
    println(s"Finishing $duration!")
    duration
  }

  def seqFutures[T, U](items: TraversableOnce[T])(yourfunction: T => Future[U]): Future[Seq[U]] = {
    items.foldLeft(Future.successful[Seq[U]](Seq())) {
      (f, item) => f.flatMap {
        x => yourfunction(item).map(_ +: x)
      }
    } map (_.reverse)
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
    val sequencedExecution = seqFutures(delaySequence)(delay => delayedFuture(delay))

    // Usually avoid Await because it is blocking, but we will use it
    // to prevent termination of the app before the results are in
    println("The final result: " + Await.result(sequencedExecution,  Duration.Inf))
  }
}
