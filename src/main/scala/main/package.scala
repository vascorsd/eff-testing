import cats.Eval
import monix.eval.Task
import org.atnos.eff._

package object main {
  type AppStack1 = Fx.fx1[Task]
  type AppEffect1[R] = Eff[AppStack1, R]

  type AppStack2 = Fx.fx2[Task, Memoized]
  type AppEffect2[R] = Eff[AppStack2, R]
}
