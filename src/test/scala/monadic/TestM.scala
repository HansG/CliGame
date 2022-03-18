package monadic

import org.junit.Assert.*
import org.junit.Test
import scala.collection.mutable.ListBuffer

object TestM:
  def t1(): Unit =
    val closeRecords =  ListBuffer[Int]()

    class MyAutoCloseable(val n: Int) extends AutoCloseable {
      override def close(): Unit = closeRecords += n
    }

    val result: MResource[Int] = for {
      i1 <- mresource(new MyAutoCloseable(1))
      i2 <- mresource(new MyAutoCloseable(2))
    } yield i1.n + i2.n

    assertArrayEquals(closeRecords.toArray , Array(2, 1))  
    assertEquals(result.get , 3)  
