package breeze.linalg

import org.scalatest.FunSuite

/**
 * @author ktakagaki
 * @date 3/1/14.
 */
class EnrichedTupleTest extends FunSuite {

  test("Literal vector test") {
    val testTuple1 = (0, 1, 2, 3, 4).v
    assert( testTuple1 === DenseVector(0, 1, 2, 3, 4) )
    val testTuple2 = (0.0, 1.0, 2.0, 3.0, 4d).v
    assert( testTuple2 === DenseVector(0d, 1d, 2d, 3d, 4d) )
    //println(testTuple2)
    val testTuple3 = (0, 1.5, 2, 3, 4).v
    assert( testTuple3 === DenseVector(0d, 1.5d, 2d, 3d, 4d) )

  }



}
