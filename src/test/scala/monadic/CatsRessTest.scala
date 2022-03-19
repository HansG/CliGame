package monadic

import weaver.SimpleIOSuite
import cats.effect.{IO, IOApp, Resource}
import munit.FunSuite



def mkResource(s: String) = {
  val acquire = IO(Console.println(s"Acquiring $$s")) *> IO.pure(s)
  def release(s: String) = IO(println(s"Releasing $$s"))
  Resource.make(acquire)(release)
}

val r = for {
  outer <- mkResource("outer")
  inner <- mkResource("inner")
} yield (outer, inner)


class CatsRessTest extends SimpleIOSuite :

  test("ress allocation and release") {
    r.use { case (a, b) =>
      IO(println(s"Using $$a and $$b")) >> IO(expect(1 == 1))
    } 
  }
