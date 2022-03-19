package types

import scala.compiletime.ops.int.*

object MyRefine {

  trait Validated[PredRes <: Boolean]
  given Validated[true] = new Validated[true] {}

  trait RefinedInt[Predicate[_ <: Int] <: Boolean]
  def validate[V <: Int, Predicate[_ <: Int] <: Boolean]
    (using Validated[Predicate[V]]): RefinedInt[Predicate] = new RefinedInt {}

  type LowerThan10[V <: Int] = V < 10
  val lowerThan10: RefinedInt[LowerThan10] = validate[4, LowerThan10]
  val nlowerThan10  = validate[-5, LowerThan10]


  val lowerThan11 = validate[4, [V <: Int] =>> V < 10]



}
