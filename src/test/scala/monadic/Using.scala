package monadic
import reflect.Selectable.reflectiveSelectable
import scala.util.{Failure, Success, Try}


  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

def tryUsingRead(filename: String): Try[List[String]] = {
  Try {
    val lines = using(io.Source.fromFile(filename)) { source =>
      (for (line <- source.getLines) yield line).toList
    }
    lines
  }
}


object TestUsing extends App {
  using(io.Source.fromFile("example.txt")) { source => {
    for (line <- source.getLines) {
      println(line)
    }
  }}

  val passwdFile = readTextFileWithTry("/etc/passwd")
  passwdFile match {
    case Success(lines) => lines.foreach(println)
    case Failure(s) => println(s"Failed, message is: $s")
  }
}



