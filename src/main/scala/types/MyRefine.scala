package types

import scala.compiletime.ops.int.*

object MyRefine {

  import scala.compiletime.ops.int.*
  import scala.compiletime.ops.boolean.*

  trait Validated[PredRes <: Boolean]
  given Validated[true] = new Validated[true] {}

  trait RefinedInt[Predicate[_ <: Int] <: Boolean]
  def validate[V <: Int, Predicate[_ <: Int] <: Boolean](using Validated[Predicate[V]]): RefinedInt[Predicate] = new RefinedInt {}
  def validate1[V <: Int, Predicate[_ <: Int] <: Boolean](using Validated[Predicate[V]])  = true
  def id[V <: Int](using Validated[V < 10])(v:V): V = v

  id[5](5)
  validate1[7, [V <: Int] =>> V > 5 && V < 10]
 // validate1[11, [V <: Int] =>> V > 5 && V < 10]



  val lowerThan10: RefinedInt[[V <: Int] =>> V < 10] = validate[4, [V <: Int] =>> V < 10]
  type LowerThan10[V <: Int] = V < 10
  val lowerThan10x = validate[4, LowerThan10]
  val lowerThan102 = validate1[4, LowerThan10]
  val lowerThan9 = validate[7, LowerThan10]
  val nlowerThan10 = validate[7, LowerThan10]
  val lowerThan10a = validate[-4, LowerThan10]


}
