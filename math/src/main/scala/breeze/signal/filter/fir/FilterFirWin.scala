package breeze.signal

import breeze.linalg.DenseVector
import breeze.signal.support.{CanFirwin, FIRKernel1D}
import breeze.signal._

object FilterFirWin {

  def apply(order: OptOrder, omega: OptOmega, samplingRate: Double = 2d,
            zeroPass: Boolean = true,
            scale: Boolean = true, multiplier: Double = 1d,
            optWindow: OptWindowFunction = OptWindowFunction.Hamming())(data: DenseVector[Double]): DenseVector[Double] = ???

  def design[Input](order: OptOrder, omega: OptOmega, samplingRate: Double = 2d,
                zeroPass: Boolean = true,
                scale: Boolean = true, multiplier: Double = 1d,
                optWindow: OptWindowFunction = OptWindowFunction.Hamming()  )
               (implicit canFirwin: CanFirwin[Input]): FIRKernel1D[Input] = ???

//    omega match {
//
//                  case o: OptOmega.DoubleValue => canFirwin(taps, DenseVector(o.omega), samplingRate, zeroPass, scale, multiplier, optWindow)
//                  case o: OptOmega.TupleValue => canFirwin(taps, DenseVector(o.omega1, o.omega2), samplingRate, zeroPass, scale, multiplier, optWindow)
//  }
   
}