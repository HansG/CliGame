package monadic

import cats.effect.IO
import weaver.SimpleIOSuite

class CatsRessTest extends SimpleIOSuite :

  test("ress allocation and release") {
    r.use { case (a, b) =>
      IO(println(s"Using $$a and $$b"))
    }
  }
