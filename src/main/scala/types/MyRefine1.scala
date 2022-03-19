package types

object MyRefine1 {
  import scala.compiletime.*
  import scala.compiletime.ops.boolean.*
  import scala.compiletime.ops.int.*

  type PredFun = [V <: Int] =>> Boolean
  trait Fun
  class PFun[E[_] <: PredFun[_]] extends Fun
   type LT10   = [V <: Int] =>> V < 10
   new PFun[LT10]{}
  trait Validated[E[_] <: PredFun[_]]
/*  val v = new Validated[LT10]{}
Type argument types.MyRefine1.LT10 does not conform to upper bound [_] =>> Boolean
  val v = new Validated[LT10]{}
 */

  // given Validated[true] = new Validated[true] {}
  implicit inline def mkVal[V <: Int & Singleton, E[_] <: PredFun[_]](v: V): Validated[E] =
    inline erasedValue[E[V]] match
      case _: true =>
         new Validated[E] {}
      case _: false =>
        inline val vs    = constValue[ToString[V]]
        inline val limit = -1 //ToString[E]
        error("Validation failed: " + vs + " < " + limit)

  // val idv : Validated[LT10] = 9

  /*
  type PF[_ <: Int] <: Boolean
  trait Valid[E <: PF[_]]
  type L10[V <: Int] = V < 10
  val v1 = new Valid[L10[5]]{}
*/



 // idv(5)



}
