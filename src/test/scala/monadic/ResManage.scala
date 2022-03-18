package monadic

def mresource[C](closeable: => C) = new MResource(closeable)


class MResource[C](closeable: => C)(using closer: Closer[C]): 

  def apply[R](f: C => R): R = 
    val result = f(closeable)
    closer.close(closeable)
    result

  def flatMap[B: Closer](f: C => MResource[B]): MResource[B] = apply(f)

  def map[B: Closer](f: C => B): MResource[B] = flatMap(c => new MResource(f(c)))

  def get: C = apply(identity)



trait Closer[C] :
  def close(closeable: C): Unit

object Closer :
  given acl[C <: AutoCloseable]: Closer[C] =  new Closer[C]:
      def close(closeable: C): Unit = closeable.close()

  given ncl[T]: Closer[T] = new Closer[T] :
    def close(closeable: T): Unit = ()
    
