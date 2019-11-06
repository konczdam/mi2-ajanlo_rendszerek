import java.io.Serializable;
import java.util.Arrays;

public class Array2DRowRealMatrix extends AbstractRealMatrix implements Serializable {
    private static final long serialVersionUID = -1067294169172445528L;
    private double[][] data;

    public Array2DRowRealMatrix() {
    }

    public Array2DRowRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        super(rowDimension, columnDimension);
        this.data = new double[rowDimension][columnDimension];
    }

    public Array2DRowRealMatrix(double[][] d) throws Exception {
        this.copyIn(d);
    }

    public Array2DRowRealMatrix(double[][] d, boolean copyArray) throws Exception {
        if (copyArray) {
            this.copyIn(d);
        } else {
            if (d == null) {
                throw new NullArgumentException();
            }

            int nRows = d.length;
            if (nRows == 0) {
                throw new Exception();
            }

            int nCols = d[0].length;
            if (nCols == 0) {
                throw new Exception();
            }

            for(int r = 1; r < nRows; ++r) {
                if (d[r].length != nCols) {
                    throw new Exception();
                }
            }

            this.data = d;
        }

    }

    public Array2DRowRealMatrix(double[] v) {
        int nRows = v.length;
        this.data = new double[nRows][1];

        for(int row = 0; row < nRows; ++row) {
            this.data[row][0] = v[row];
        }

    }

    public RealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new Array2DRowRealMatrix(rowDimension, columnDimension);
    }

    public RealMatrix copy() throws Exception {
        return new Array2DRowRealMatrix(this.copyOut(), false);
    }

    public Array2DRowRealMatrix add(Array2DRowRealMatrix m) throws Exception {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        double[][] outData = new double[rowCount][columnCount];

        for(int row = 0; row < rowCount; ++row) {
            double[] dataRow = this.data[row];
            double[] mRow = m.data[row];
            double[] outDataRow = outData[row];

            for(int col = 0; col < columnCount; ++col) {
                outDataRow[col] = dataRow[col] + mRow[col];
            }
        }

        return new Array2DRowRealMatrix(outData, false);
    }

    public Array2DRowRealMatrix subtract(Array2DRowRealMatrix m) throws Exception {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        double[][] outData = new double[rowCount][columnCount];

        for(int row = 0; row < rowCount; ++row) {
            double[] dataRow = this.data[row];
            double[] mRow = m.data[row];
            double[] outDataRow = outData[row];

            for(int col = 0; col < columnCount; ++col) {
                outDataRow[col] = dataRow[col] - mRow[col];
            }
        }

        return new Array2DRowRealMatrix(outData, false);
    }

    public Array2DRowRealMatrix multiply(Array2DRowRealMatrix m) throws Exception {
        int nRows = this.getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = this.getColumnDimension();
        double[][] outData = new double[nRows][nCols];
        double[] mCol = new double[nSum];
        double[][] mData = m.data;

        for(int col = 0; col < nCols; ++col) {
            int row;
            for(row = 0; row < nSum; ++row) {
                mCol[row] = mData[row][col];
            }

            for(row = 0; row < nRows; ++row) {
                double[] dataRow = this.data[row];
                double sum = 0.0D;

                for(int i = 0; i < nSum; ++i) {
                    sum += dataRow[i] * mCol[i];
                }

                outData[row][col] = sum;
            }
        }

        return new Array2DRowRealMatrix(outData, false);
    }

    public double[][] getData() {
        return this.copyOut();
    }

    @Override
    public double getNorm() {
        return 0;
    }

    @Override
    public double getFrobeniusNorm() {
        return 0;
    }

    @Override
    public RealMatrix getSubMatrix(int[] var1, int[] var2) throws NullArgumentException, NoDataException, OutOfRangeException, NotStrictlyPositiveException {
        return null;
    }

    @Override
    public void copySubMatrix(int var1, int var2, int var3, int var4, double[][] var5) throws OutOfRangeException, NumberIsTooSmallException, MatrixDimensionMismatchException {

    }

    @Override
    public void copySubMatrix(int[] var1, int[] var2, double[][] var3) throws OutOfRangeException, NullArgumentException, NoDataException, MatrixDimensionMismatchException {

    }

    @Override
    public void setSubMatrix(double[][] var1, int var2, int var3) throws Exception {

    }

    public double[][] getDataRef() {
        return this.data;
    }



    @Override
    public RealMatrix getRowMatrix(int var1) throws OutOfRangeException {
        return null;
    }

    @Override
    public void setRowMatrix(int var1, RealMatrix var2) throws OutOfRangeException, MatrixDimensionMismatchException {

    }

    @Override
    public RealMatrix getColumnMatrix(int var1) throws OutOfRangeException {
        return null;
    }

    @Override
    public void setColumnMatrix(int var1, RealMatrix var2) throws OutOfRangeException, MatrixDimensionMismatchException {

    }

    public double getEntry(int row, int column) throws OutOfRangeException {
        return this.data[row][column];
    }

    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        this.data[row][column] = value;
    }

    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        double[] var10000 = this.data[row];
        var10000[column] += increment;
    }

    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        double[] var10000 = this.data[row];
        var10000[column] *= factor;
    }

    @Override
    public RealMatrix transpose() {
        return null;
    }

    @Override
    public double getTrace() throws NonSquareMatrixException {
        return 0;
    }

    public int getRowDimension() {
        return this.data == null ? 0 : this.data.length;
    }

    public int getColumnDimension() {
        return this.data != null && this.data[0] != null ? this.data[0].length : 0;
    }

    public double[] operate(double[] v) throws Exception {
        int nRows = this.getRowDimension();
        int nCols = this.getColumnDimension();
        if (v.length != nCols) {
            throw new Exception();
        } else {
            double[] out = new double[nRows];

            for(int row = 0; row < nRows; ++row) {
                double[] dataRow = this.data[row];
                double sum = 0.0D;

                for(int i = 0; i < nCols; ++i) {
                    sum += dataRow[i] * v[i];
                }

                out[row] = sum;
            }

            return out;
        }
    }

    public double[] preMultiply(double[] v) throws Exception {
        int nRows = this.getRowDimension();
        int nCols = this.getColumnDimension();
        if (v.length != nRows) {
            throw new Exception();
        } else {
            double[] out = new double[nCols];

            for(int col = 0; col < nCols; ++col) {
                double sum = 0.0D;

                for(int i = 0; i < nRows; ++i) {
                    sum += this.data[i][col] * v[i];
                }

                out[col] = sum;
            }

            return out;
        }
    }


    private double[][] copyOut() {
        int nRows = this.getRowDimension();
        double[][] out = new double[nRows][this.getColumnDimension()];

        for(int i = 0; i < nRows; ++i) {
            System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
        }

        return out;
    }

    private void copyIn(double[][] in) throws Exception {
        this.setSubMatrix(in, 0, 0);
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        String fullClassName = this.getClass().getName();
        String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(46) + 1);
        res.append(shortClassName);
        for (int i = 0; i < this.getRowDimension(); i++) {
            res.append("\n ");
            for (int j = 0; j < this.getColumnDimension() ; j++) {
                res.append(this.data[i][j] + "  ") ;
            }
        }
        return res.toString();
    }
}
