package clig

import cats.{Eq, Show}
import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.syntax.foldable.toFoldableOps
import cats.syntax.all.toBifunctorOps

import scala.collection.immutable.ArraySeq


val BoardSize = 3

enum Player:
  case X
  case O

  def next = this match
    case X => O
    case O => X

object Player:
  given Show[Player] = Show.fromToString
  given Eq[Player] = Eq.fromUniversalEquals


enum FieldStatus:
  case Empty
  case Taken(player : Player)

type Direction = (-1 | 0 | 1, -1 | 0 | 1)
object Direction:
  // left
  val L: Direction = (-1, 0)
  // right
  val R: Direction = (1, 0)
  // top
  val T: Direction = (0, -1)
  // bottom
  val B: Direction = (0, 1)
  // left top
  val LT: Direction = (-1, -1)
  // right top
  val RT: Direction = (1, -1)
  // left bottom
  val LB: Direction = (-1, 1)
  // right bottom
  val RB: Direction = (1, 1)



final case class Coordinate(x: Int, y: Int):
  def translate(d: Direction): Option[Coordinate] = d.bimap(_ + x, _ + y) match
    case (nx, ny) =>
      Option.when(nx >= 0 && nx < BoardSize && ny >= 0 && ny < BoardSize)(
        Coordinate(nx, ny)
      )

object Coordinate:
  val Letters = LazyList.from('A').take(BoardSize).toVector
  def parse(s:String) : Option[Coordinate] =

    def fromChar(c:Char) = Letters.indexOf(c.toInt) match
      case  -1  => None
      case  x  => Some(x)

    def fromDigit(c:Char) = Option.when(c.isDigit)(c.asDigit - 1)
      .filter(d => d >= 0 && d < BoardSize)

    s.toCharArray match
      case Array(c,d) => (fromChar(c), fromDigit(d)).mapN(Coordinate.apply)
      case _ => None

  given Show[Coordinate] = Show.show(c =>
    Letters.get(c.x) match
      case Some(letter) if c.y >= 0 && c.y < BoardSize  => s"${letter.toChar}${c.y + 1}"
      case _ => "--OUT OF BOUNDS--"
  )


opaque type Board = ArraySeq[ArraySeq[FieldStatus]]

object Board:
  def create: Board = ArraySeq.fill(BoardSize)(ArraySeq.fill(BoardSize)(FieldStatus.Empty))

  extension (f: Board)
    def apply(c: Coordinate): Option[FieldStatus] = for
      line <- f.get(c.y)
      cell <- line.get(c.x)
    yield cell

    def update(c: Coordinate, fieldStatus: FieldStatus): Option[Board] =
      import c.{x, y}
      for
        line <- f.get(y)
      yield f.updated(y, line.updated(x, fieldStatus))

given Show[Board] with
  def show(f: Board) =
    val numbers = LazyList.from(1).take(f.size).map(String.format("%1$2d", _))
    val letters = LazyList.from('A').take(f.size).map(_.toChar.toString)

    ("   " + letters.mkString(" ") + "\n") + f.zip(numbers).map {
      case (line, number) =>
        (number + " ") + line.map {
          case FieldStatus.Empty => " "
          case FieldStatus.Taken(Player.X) => "X"
          case FieldStatus.Taken(Player.O) => "O"
        }.mkString("|")
    }.mkString("\n   " + "- " * f.size + "\n")