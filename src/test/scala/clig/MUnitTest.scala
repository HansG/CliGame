package clig

import clig.Coordinate
import munit.FunSuite

class MUnitTest extends FunSuite:
  test("should parse correct coordinates") {

    //when
    val cases = List(
      ("A1", Some(Coordinate(0, 0))),
      ("A3", Some(Coordinate(0, 2))),
      ("B1", Some(Coordinate(1, 0))),
      ("B2", Some(Coordinate(1, 1))),
      ("B3", Some(Coordinate(1, 2))),
      ("C1", Some(Coordinate(2, 0))),
      ("C3", Some(Coordinate(2, 2)))
    )

    //then
    cases.foreach {
      case (rawString, expected) =>
        assert(clue(Coordinate.parse(rawString)) == clue(expected))
    }
  }




