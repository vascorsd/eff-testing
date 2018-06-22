package main

import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.addon.monix.task._
import org.atnos.eff.syntax.addon.monix.task._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._

object Main extends App {
  val cache: Cache = ConcurrentHashMapCache()

  def runApp1[R](app: AppEffect1[R]): Task[R] =
    app.runAsync

  def runApp2[R](app: AppEffect2[R]): Task[R] =
    app
      .runTaskMemo(cache)
      .runAsync
}


trait Alg[F[_]] {
  def something(): F[Either[String, Something]]
}

object Interpreters {
  def alg[R: _task]() = new RealSomething[R]
  def alg2[R: _task : _memo]() = new RealSomethingCached[R]
}

class RealSomething[R: _task]() extends Alg[Eff[R, ?]] {
  override def something(): Eff[R, Either[String, Something]] = {
    for {
      value <- fromTask(Task.now(Something("mom")))
    } yield Right(value)
  }
}

class RealSomethingCached[R: _task : _memo]() extends Alg[Eff[R, ?]] {
  override def something(): Eff[R, Either[String, Something]] = {
    val cacheKey = 'something
    for {
      value <- taskMemoized(cacheKey, fromTask(Task.now(Something("mom"))))
    } yield Right(value)
  }
}

case class Something(value: String)
