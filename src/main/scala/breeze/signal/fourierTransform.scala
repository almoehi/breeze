package breeze.signal

import breeze.generic.UFunc
import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.math.Complex
import breeze.signal.support.JTransformsSupport._
import breeze.macros.expand


/**
 * Returns the fast fourier transform of a DenseVector or DenseMatrix. Currently,
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
object fourierTransform extends UFunc {

  /** Use via implicit delegate syntax fft(x: DenseVector)
    *
    */
  implicit val dvDouble1DFFT : fourierTransform.Impl[DenseVector[Double], DenseVector[Complex]] = {
    new fourierTransform.Impl[DenseVector[Double], DenseVector[Complex]] {
      def apply(v: DenseVector[Double]) = {
        //reformat for input: note difference in format for input to complex fft
        val tempArr = denseVectorDToTemp(v)

        //actual action
        val fft_instance = getD1DInstance(v.length)
        fft_instance.realForwardFull( tempArr ) //does operation in place

        //reformat for output
        tempToDenseVector(tempArr)
      }
    }
  }

  /** Use via implicit delegate syntax fft(x: DenseVector)
    *     For some reason this breaks scaladoc... No good reason why.
  @expand
  @expand.valify
  implicit def dvDT1DFFT[@expand.args(Float, Long, Int) T]: Impl[DenseVector[T], DenseVector[Complex]] = {
    new Impl[DenseVector[T], DenseVector[Complex]] {
      def apply(v: DenseVector[T]) = fourierTransform( v.map( _.toDouble) )
    }
  }
    */

  implicit def dvDT1DFFT_Float: Impl[DenseVector[Float], DenseVector[Complex]] = {
    new Impl[DenseVector[Float], DenseVector[Complex]] {
      def apply(v: DenseVector[Float]) = fourierTransform( v.map( _.toDouble) )
    }
  }

  implicit def dvDT1DFFT_Int: Impl[DenseVector[Int], DenseVector[Complex]] = {
    new Impl[DenseVector[Int], DenseVector[Complex]] {
      def apply(v: DenseVector[Int]) = fourierTransform( v.map( _.toDouble) )
    }
  }

  implicit def dvDT1DFFT_Long: Impl[DenseVector[Long], DenseVector[Complex]] = {
    new Impl[DenseVector[Long], DenseVector[Complex]] {
      def apply(v: DenseVector[Long]) = fourierTransform( v.map( _.toDouble) )
    }
  }

   /** Use via implicit delegate syntax fft(x: DenseVector)
    *
    */
  implicit val dvComplex1DFFT : fourierTransform.Impl[DenseVector[Complex], DenseVector[Complex]] = {
    new fourierTransform.Impl[DenseVector[Complex], DenseVector[Complex]] {
      def apply(v: DenseVector[Complex]) = {
        //reformat for input: note difference in format for input to real fft
        val tempArr = denseVectorCToTemp(v)

        //actual action
        val fft_instance = getD1DInstance(v.length)
        fft_instance.complexForward( tempArr ) //does operation in place

        //reformat for output
        tempToDenseVector(tempArr)
      }
    }
  }

  /** Use via implicit delegate syntax fft(x: DenseMatrix)
    *
    */
  implicit val dmComplex2DFFT : fourierTransform.Impl[DenseMatrix[Complex], DenseMatrix[Complex]] = {
    new fourierTransform.Impl[DenseMatrix[Complex], DenseMatrix[Complex]] {
      def apply(v: DenseMatrix[Complex]) = {
        //reformat for input: note difference in format for input to real fft
        val tempMat = denseMatrixCToTemp(v)

        //actual action
        val fft_instance = getD2DInstance(v.rows, v.cols)
        fft_instance.complexForward( tempMat ) //does operation in place

        //reformat for output
        tempToDenseMatrix(tempMat, v.rows, v.cols)
      }
    }
  }

  /** Use via implicit delegate syntax fft(x: DenseMatrix)
    *
    */
  implicit val dmDouble2DFFT : fourierTransform.Impl[DenseMatrix[Double], DenseMatrix[Complex]] = {
    new fourierTransform.Impl[DenseMatrix[Double], DenseMatrix[Complex]] {
      def apply(v: DenseMatrix[Double]) = {
        //reformat for input
        val tempMat = denseMatrixDToTemp(v)

        //actual action
        val fft_instance = getD2DInstance(v.rows, v.cols)
        fft_instance.complexForward( tempMat ) //does operation in place
        //ToDo this could be optimized to use realFullForward for speed, but only if the indexes are powers of two

        //reformat for output
        tempToDenseMatrix(tempMat, v.rows, v.cols)
      }
    }
  }



}
