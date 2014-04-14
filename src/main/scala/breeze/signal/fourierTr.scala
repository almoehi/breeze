package breeze.signal

import breeze.linalg._
import breeze.linalg.{sum, DenseMatrix, DenseVector}
import breeze.generic.UFunc
import breeze.math.Complex
import breeze.signal.support.JTransformsSupport._
import breeze.macros.expand
import breeze.numerics.{sin, cos}

//when updating, be sure to update relevant portions of sinTr and cosTr as well
/**
 * Returns the discrete fourier transform of a DenseVector or DenseMatrix. Currently,
 * DenseVector/DenseMatrix types of Double and Complex are supported. Scaling
 * follows the common signal processing convention, i.e. <b>no scaling on forward DFT</b>,
 * and 1/n scaling for the inverse DFT. Of note, fft(x: DenseMatrix[Double]) will
 * perform the 2D fft in both row and column dimensions, as opposed to the MatLab
 * toolbox syntax, which performs column-wise 1D fft.</p>
 * Implementation is via the implicit trait fft.Impl[ InputType,  OutputType ],
 * which is found in breeze.signal.support.fft.Impl.scala.
 *
 * @return
 * @author ktakagaki, dlwh
 */
object fourierTr extends UFunc {

  //ToDo 2: Provide Float implementation wrapper for JTransforms? But need to return Complex, no ComplexFloat class

  // <editor-fold defaultstate="collapsed" desc=" DenseVector FFTs ">

  implicit val dvDouble1DFFT : fourierTr.Impl[DenseVector[Double], DenseVector[Complex]] = {
    new Impl[DenseVector[Double], DenseVector[Complex]] {
      def apply(v: DenseVector[Double]) = {
        //reformat for input: note difference in format for input to complex fft
        val tempArr = denseVectorDToTemp(v)

        //actual action
        val fft_instance = getDoubleFFT1DInst(v.length)
        fft_instance.realForwardFull( tempArr ) //does operation in place

        //reformat for output
        tempToDenseVector(tempArr)
      }
    }
  }

  implicit val dvDouble1DFFTN : fourierTr.Impl2[DenseVector[Double], Int, DenseVector[Complex]] = {
    new Impl2[DenseVector[Double], Int, DenseVector[Complex]] {
      def apply(v: DenseVector[Double], n: Int) = {
        val len = v.length
        dvDouble1DFFT(
          if(len == n)     v
          else if(len > n) v(0 to n)
          else             DenseVector.vertcat( v, DenseVector.zeros[Double]( n-len ) )
        )
      }
    }
  }



  @expand
  implicit def dvDT1DFFTT[@expand.args(Float, Int, Long) T]: Impl[DenseVector[T], DenseVector[Complex]] = {
    new Impl[DenseVector[T], DenseVector[Complex]] {
      def apply(v: DenseVector[T]) = fourierTr( convert(v, Double) )
    }
  }

  implicit val dvComplex1DFFT : fourierTr.Impl[DenseVector[Complex], DenseVector[Complex]] = {
    new fourierTr.Impl[DenseVector[Complex], DenseVector[Complex]] {
      def apply(v: DenseVector[Complex]) = {
        //reformat for input: note difference in format for input to real fft
        val tempArr = denseVectorCToTemp(v)

        //actual action
        val fft_instance = getDoubleFFT1DInst(v.length)
        fft_instance.complexForward( tempArr ) //does operation in place

        //reformat for output
        tempToDenseVector(tempArr)
      }
    }
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc=" DenseMatrix FFTs ">

  implicit val dmComplex2DFFT : fourierTr.Impl[DenseMatrix[Complex], DenseMatrix[Complex]] = {
    new fourierTr.Impl[DenseMatrix[Complex], DenseMatrix[Complex]] {
      def apply(v: DenseMatrix[Complex]) = {
        //reformat for input: note difference in format for input to real fft
        val tempMat = denseMatrixCToTemp(v)

        //actual action
        val fft_instance = getDoubleFFT2DInst(v.rows, v.cols)
        fft_instance.complexForward( tempMat ) //does operation in place

        //reformat for output
        tempToDenseMatrix(tempMat, v.rows, v.cols)
      }
    }
  }

  implicit val dmDouble2DFFT : fourierTr.Impl[DenseMatrix[Double], DenseMatrix[Complex]] = {
    new fourierTr.Impl[DenseMatrix[Double], DenseMatrix[Complex]] {
      def apply(v: DenseMatrix[Double]) = {
        //reformat for input
        val tempMat = denseMatrixDToTemp(v)

        //actual action
        val fft_instance = getDoubleFFT2DInst(v.rows, v.cols)
        fft_instance.complexForward( tempMat ) //does operation in place
        //ToDo this could be optimized to use realFullForward for speed, but only if the indexes are powers of two

        //reformat for output
        tempToDenseMatrix(tempMat, v.rows, v.cols)
      }
    }
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc=" Fourier extracting only specified taps ">

  implicit val dvDouble1DFourierRange: Impl2[DenseVector[Double], Range, DenseVector[Complex]] = {
    new Impl2[DenseVector[Double], Range, DenseVector[Complex]] {
      def apply(v: DenseVector[Double], rangeNegative: Range ): DenseVector[Complex] = {

        val range = rangeNegative.getRangeWithoutNegativeIndexes( v.length )
        //ToDo check lengths and throw errors

        val tempret =
          for( k <- range ) yield {
            val pk2_N = scala.math.Pi * k * 2d / v.length
              sum(DenseVector.tabulate[Complex](v.length)( (n: Int) => {
                                        val nd = n.toDouble
                                        Complex(cos(pk2_N * nd), sin(pk2_N * nd))
                                                                                      } ))
             }

        new DenseVector[Complex]( tempret.toArray[Complex] )

      }
    }
  }

  // </editor-fold>

}
