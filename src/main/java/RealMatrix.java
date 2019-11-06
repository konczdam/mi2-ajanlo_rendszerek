

public interface RealMatrix extends AnyMatrix {
    RealMatrix createMatrix(int var1, int var2) throws NotStrictlyPositiveException;

    RealMatrix copy() throws Exception;

    RealMatrix add(RealMatrix var1) throws MatrixDimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException;

    RealMatrix subtract(RealMatrix var1) throws MatrixDimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException;

    RealMatrix scalarAdd(double var1) throws NotStrictlyPositiveException, OutOfRangeException;

    RealMatrix scalarMultiply(double var1) throws NotStrictlyPositiveException, OutOfRangeException;

    RealMatrix multiply(RealMatrix var1) throws DimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException;

    RealMatrix preMultiply(RealMatrix var1) throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException;

    RealMatrix power(int var1) throws Exception;

    double[][] getData() throws OutOfRangeException;

    double getNorm();

    double getFrobeniusNorm();

    RealMatrix getSubMatrix(int var1, int var2, int var3, int var4) throws OutOfRangeException, NumberIsTooSmallException, NotStrictlyPositiveException;

    RealMatrix getSubMatrix(int[] var1, int[] var2) throws NullArgumentException, NoDataException, OutOfRangeException, NotStrictlyPositiveException;

    void copySubMatrix(int var1, int var2, int var3, int var4, double[][] var5) throws OutOfRangeException, NumberIsTooSmallException, MatrixDimensionMismatchException;

    void copySubMatrix(int[] var1, int[] var2, double[][] var3) throws OutOfRangeException, NullArgumentException, NoDataException, MatrixDimensionMismatchException;

    void setSubMatrix(double[][] var1, int var2, int var3) throws Exception;

    RealMatrix getRowMatrix(int var1) throws OutOfRangeException;

    void setRowMatrix(int var1, RealMatrix var2) throws OutOfRangeException, MatrixDimensionMismatchException;

    RealMatrix getColumnMatrix(int var1) throws OutOfRangeException;

    void setColumnMatrix(int var1, RealMatrix var2) throws OutOfRangeException, MatrixDimensionMismatchException;

//    RealVector getRowVector(int var1) throws OutOfRangeException;

//    void setRowVector(int var1, RealVector var2) throws OutOfRangeException, MatrixDimensionMismatchException;

//    RealVector getColumnVector(int var1) throws OutOfRangeException;

//    void setColumnVector(int var1, RealVector var2) throws OutOfRangeException, MatrixDimensionMismatchException;

    double[] getRow(int var1) throws OutOfRangeException;

    void setRow(int var1, double[] var2) throws OutOfRangeException, MatrixDimensionMismatchException;

    double[] getColumn(int var1) throws OutOfRangeException;

    void setColumn(int var1, double[] var2) throws OutOfRangeException, MatrixDimensionMismatchException;

    double getEntry(int var1, int var2) throws OutOfRangeException;

    void setEntry(int var1, int var2, double var3) throws OutOfRangeException;

    void addToEntry(int var1, int var2, double var3) throws OutOfRangeException;

    void multiplyEntry(int var1, int var2, double var3) throws OutOfRangeException;

    RealMatrix transpose();

    double getTrace() throws NonSquareMatrixException;

    double[] operate(double[] var1) throws Exception;

//    RealVector operate(RealVector var1) throws DimensionMismatchException;

    double[] preMultiply(double[] var1) throws Exception;

//    RealVector preMultiply(RealVector var1) throws DimensionMismatchException;
//
//    double walkInRowOrder(RealMatrixChangingVisitor var1);
//
//    double walkInRowOrder(RealMatrixPreservingVisitor var1);
//
//    double walkInRowOrder(RealMatrixChangingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;
//
//    double walkInRowOrder(RealMatrixPreservingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;
//
//    double walkInColumnOrder(RealMatrixChangingVisitor var1);
//
//    double walkInColumnOrder(RealMatrixPreservingVisitor var1);
//
//    double walkInColumnOrder(RealMatrixChangingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;
//
//    double walkInColumnOrder(RealMatrixPreservingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;
//
//    double walkInOptimizedOrder(RealMatrixChangingVisitor var1);
//
//    double walkInOptimizedOrder(RealMatrixPreservingVisitor var1);
//
//    double walkInOptimizedOrder(RealMatrixChangingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;
//
//    double walkInOptimizedOrder(RealMatrixPreservingVisitor var1, int var2, int var3, int var4, int var5) throws OutOfRangeException, NumberIsTooSmallException;


}