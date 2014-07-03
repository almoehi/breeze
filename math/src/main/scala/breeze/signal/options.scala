package breeze.signal

import breeze.linalg.DenseVector
import breeze.util.Opt

/**Specifies all possible option objects for the breeze.signal package
  *
 * @author ktakagaki
 */


///General options-related

//Generic options with implicit specialization cannot be employed for case objects
// (they can for parameterless case classes)
//
///**Generic option for none.*/
//case object None extends Opt {
//  //these implicit conversions will allow breeze.signal.None() object to be given for different functions.
//  implicit def optNoneSpecialize_WindowFunction(x: breeze.signal.None) = OptWindowFunction.None
//  implicit def optNoneSpecialize_ConvolveOverhang(x: breeze.signal.None) = OptOverhang.None
//  //implicit def optNoneSpecialize_Padding(x: breeze.signal.None) = breeze.signal.OptPadding.None()
//}
//
///**Generic option for automatic.*/
//case object Automatic extends Opt {
//  //these implicit conversions will allow breeze.signal.None() object to be given for different functions.
//  implicit def optNoneSpecialize_ConvolveMethod(x: breeze.signal.Automatic) = OptMethod.Automatic
//}


///Individual Options

/**Option values: window function for filter design.*/
abstract class OptWindowFunction extends Opt
object OptWindowFunction {
  case class Hamming(alpha: Double = 0.54, beta: Double = 0.46) extends OptWindowFunction {
    override def toString = "Hamming window ("+ alpha + ", " + beta + ")"
  }
  case class User(dv: DenseVector[Double]) extends OptWindowFunction {
    override def toString = "user-specified window"
  }
  case object None extends OptWindowFunction{
    override def toString = "no window"
  }
}


/**Options: how to deal with filter/convolution overhangs.*/
abstract class OptOverhang extends Opt
object OptOverhang{
  /**Default, no overhangs.*/
  case object None extends OptOverhang
  /**Maximal overhangs, equivalent to MatLab conv default ('full').*/
  case object Full extends OptOverhang
  /**Preserve length: pads both size with a fixed value (e.g. 0) to provide
    * filter results which are the same length as filter input data.*/
  case object PreserveLength extends OptOverhang
  /**Cyclical padding, also preserves length.*/
  case object Cyclical extends OptOverhang
}

/**Options: how to deal with filter/convolution padding, only valid for
  * OptOverhang.Full and OptOverhang.PreserveLength.*/
abstract class OptPadding extends Opt
object OptPadding{
  /**Option value: Pads with 0.*/
  case object Zero extends OptPadding
  /**Pads with a specific value, eg 0.*/
  case class ValueOpt[T](value: T) extends OptPadding
  /**Pads with boundary values: the first and last components of the data.*/
  case object Boundary extends OptPadding
}

/**Option values: how to deal with convolution and filter padding.*/
abstract class OptMethod extends Opt
object OptMethod{
  /**Option value: Decides on the fastest convolve method based on data size and type.*/
  case object Automatic extends OptMethod
  /**Option value: Convolve using FFT.*/
  case object FFT extends OptMethod
  /**Option value: Convolve using for loop.*/
}

abstract class OptDesignMethod extends Opt
object OptDesignMethod {
  /**Option value: use firwin() to design FIR kernel using window method.*/
  case object Firwin extends OptDesignMethod
  case object Cheby1 extends OptDesignMethod
}

abstract class OptFilterTaps extends Opt
object OptFilterTaps {
  case object Automatic extends OptFilterTaps
  case class IntOpt(n: Int) extends OptFilterTaps
}

/**slices specific result ranges out of results for convolve, etc*/
abstract class OptRange extends Opt
object OptRange {
  case object All extends OptRange {
    override def toString() = "OptRange.All"
  }
  case class RangeOpt(r: Range) extends OptRange {
    override def toString() = "OptRange.RangeOpt( "+ r.start + ", "+ r.end+", "+r.step +"), isInclusive=" + r.isInclusive
  }
//  case class Single(i: Int) extends OptRange {
//    override def toString() = "OptRange.Single("+ i +")"
//  }
  implicit def rangeToRangeOpt(r: Range) = OptRange.RangeOpt( r )
}

