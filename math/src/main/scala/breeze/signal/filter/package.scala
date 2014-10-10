package breeze.signal

import breeze.signal.support._
import breeze.linalg.DenseVector
import breeze.numerics.isEven
import breeze.macros.expand

package object filter {
  
  // implicit conversion for options
  implicit def intToOptOrder(n: Int): OptOrder = new OptOrder.IntValue(n)
  // implicit conversion for filter cuttoffs
  // If sampling rate is the default Nyqist frequency (i.e. samplingRate == 2),
  // values must be in (0,1)
  implicit def doubleToOptOmega(omega: Double): OptOmega = new OptOmega.DoubleValue(omega)
  //can be specified as a Tuple of Doubles for band-stop or band-pass filters
  //(i.e. filters with 2 cutoff frequencies
  implicit def tuple2ToOptOmega(omega: Tuple2[Double, Double]):OptOmega = new OptOmega.TupleValue(omega._1, omega._2)
  
//  // design shorthands for filter design
//  def designFiltButterworth[Input](order: OptOrder, omega: OptOmega, tpe: OptFilterTpe) = FilterButterworth.design[Input](order, omega, tpe)
  
  
}
