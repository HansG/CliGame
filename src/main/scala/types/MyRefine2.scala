package types

object MyRefine2 {
  import scala.compiletime.*
  import scala.compiletime.ops.boolean.*
  import scala.compiletime.ops.int.*

  sealed trait Pred
  class And[A <: Pred, B <: Pred]         extends Pred
  class Leaf                              extends Pred
  class LowerThan[T <: Int & Singleton]   extends Leaf
  class GreaterThan[T <: Int & Singleton] extends Leaf

  trait Validated[PredRes <: Pred]
  // given Validated[true] = new Validated[true] {}

  implicit inline def mkVal[V <: Int & Singleton, E <: Pred](v: V): Validated[E] =
    inline erasedValue[E] match
      case _: LowerThan[t] =>
        inline if constValue[V] < constValue[t]
        then new Validated[E] {}
        else
          inline val vs    = constValue[ToString[V]]
          inline val limit = constValue[ToString[t]]
          error("Validation failed: " + vs + " < " + limit)
      case _: GreaterThan[t] =>
        inline if constValue[V] > constValue[t]
        then new Validated[E] {}
        else
          inline val vs    = constValue[ToString[V]]
          inline val limit = constValue[ToString[t]]
          error("Validation failed: " + vs + " > " + limit)
      case _: And[a, b] =>
        inline mkVal[V, a](v) match
          case _: Validated[_] =>
            inline mkVal[V, b](v) match
              case _: Validated[_] => new Validated[E] {}

  val a: Validated[LowerThan[10]] = 6
  val b: Validated[GreaterThan[5] And LowerThan[10]] = 6

  import scala.compiletime.testing.typeCheckErrors

  /*
  class IntSpec extends munit.FunSuite:
    test("Those should not compile") {
      val errs1 = typeCheckErrors("val b1: Validated[GreaterThan[5] And LowerThan[10]] = 4")
      assertEquals(errs1.map(_.message), List("Validation failed: 4 > 5"))
      val errs = typeCheckErrors("val x: Validated[LowerThan[10]] = 16")
      assertEquals(errs.map(_.message), List("Validation failed: 16 < 10"))
    }
*/


}
