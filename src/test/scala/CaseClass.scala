object CaseClass {


  case class Foo(a: String, b: String) {}
  // !! ..extends ( .. => Foo ) damit  Foo.tupled compiliert!!
  // damit default companion erweitert - nicht ersetzt - wird!!!
  object Foo extends ( (String, String) => Foo ){}
  //Methode aus default companion:
  Foo.tupled

  final case class Longitude(value: Double) {
    require(value >= -180.0d, s"value [$value] must greater than or equal to -180.0d")
    require(value <= 180.0d, s"value [$value] must be less than or equal to 180.0d")
  }
  object Longitude extends (Double => Longitude) {
    def apply(value: Double): Longitude =
      new Longitude(value)

    //alternativ zu require:
    def generateInvalidStateErrors(value: Double): List[String] =
      if (value < -180.0d)
        List(s"value of value [$value] must be not be less than -180.0d")
      else
        if (value > 180.0d)
          List(s"value of value [$value] must be not be greater than 180.0d")
        else
          Nil

    def apply2(value: Double): Longitude =
      generateInvalidStateErrors(value) match {
        case Nil =>
          new Longitude(value)
        case invalidStateErrors =>
          throw new IllegalStateException(invalidStateErrors.mkString("|"))
      }
  }

}


