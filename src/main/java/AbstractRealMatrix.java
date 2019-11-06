import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public abstract class AbstractRealMatrix  implements RealMatrix {

    protected AbstractRealMatrix() {
    }

    protected AbstractRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        if (rowDimension < 1) {
            throw new NotStrictlyPositiveException(rowDimension);
        } else if (columnDimension < 1) {
            throw new NotStrictlyPositiveException(columnDimension);
        }
    }

    public RealMatrix add(RealMatrix m) throws MatrixDimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        RealMatrix out = this.createMatrix(rowCount, columnCount);

        for(int row = 0; row < rowCount; ++row) {
            for(int col = 0; col < columnCount; ++col) {
                out.setEntry(row, col, this.getEntry(row, col) + m.getEntry(row, col));
            }
        }

        return out;
    }

    public RealMatrix subtract(RealMatrix m) throws MatrixDimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        RealMatrix out = this.createMatrix(rowCount, columnCount);

        for(int row = 0; row < rowCount; ++row) {
            for(int col = 0; col < columnCount; ++col) {
                out.setEntry(row, col, this.getEntry(row, col) - m.getEntry(row, col));
            }
        }

        return out;
    }

    public RealMatrix scalarAdd(double d) throws NotStrictlyPositiveException, OutOfRangeException {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        RealMatrix out = this.createMatrix(rowCount, columnCount);

        for(int row = 0; row < rowCount; ++row) {
            for(int col = 0; col < columnCount; ++col) {
                out.setEntry(row, col, this.getEntry(row, col) + d);
            }
        }

        return out;
    }

    public RealMatrix scalarMultiply(double d) throws NotStrictlyPositiveException, OutOfRangeException {
        int rowCount = this.getRowDimension();
        int columnCount = this.getColumnDimension();
        RealMatrix out = this.createMatrix(rowCount, columnCount);

        for(int row = 0; row < rowCount; ++row) {
            for(int col = 0; col < columnCount; ++col) {
                out.setEntry(row, col, this.getEntry(row, col) * d);
            }
        }

        return out;
    }

    public RealMatrix multiply(RealMatrix m) throws DimensionMismatchException, NotStrictlyPositiveException, OutOfRangeException {
        int nRows = this.getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = this.getColumnDimension();
        RealMatrix out = this.createMatrix(nRows, nCols);

        for(int row = 0; row < nRows; ++row) {
            for(int col = 0; col < nCols; ++col) {
                double sum = 0.0D;

                for(int i = 0; i < nSum; ++i) {
                    sum += this.getEntry(row, i) * m.getEntry(i, col);
                }

                out.setEntry(row, col, sum);
            }
        }

        return out;
    }

    public RealMatrix preMultiply(RealMatrix m) throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        return m.multiply(this);
    }

    public RealMatrix power(int p) throws Exception {
        if (p < 0) {
            throw new Exception();
        } else if (!this.isSquare()) {
            throw new Exception();
        } else if (p == 0) {
            return this;
        } else if (p == 1) {
            return this.copy();
        } else {
            int power = p - 1;
            char[] binaryRepresentation = Integer.toBinaryString(power).toCharArray();
            ArrayList<Integer> nonZeroPositions = new ArrayList();
            int maxI = -1;

            int pos;
            for(int i = 0; i < binaryRepresentation.length; ++i) {
                if (binaryRepresentation[i] == '1') {
                    pos = binaryRepresentation.length - i - 1;
                    nonZeroPositions.add(pos);
                    if (maxI == -1) {
                        maxI = pos;
                    }
                }
            }

            RealMatrix[] results = new RealMatrix[maxI + 1];
            results[0] = this.copy();

            for(pos = 1; pos <= maxI; ++pos) {
                results[pos] = results[pos - 1].multiply(results[pos - 1]);
            }

            RealMatrix result = this.copy();

            Integer i;
            for(Iterator i$ = nonZeroPositions.iterator(); i$.hasNext(); result = result.multiply(results[i])) {
                i = (Integer)i$.next();
            }

            return result;
        }
    }

    public double[][] getData() throws OutOfRangeException {
        double[][] data = new double[this.getRowDimension()][this.getColumnDimension()];

        for(int i = 0; i < data.length; ++i) {
            double[] dataI = data[i];

            for(int j = 0; j < dataI.length; ++j) {
                dataI[j] = this.getEntry(i, j);
            }
        }

        return data;
    }





    public RealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException, NotStrictlyPositiveException {

        RealMatrix subMatrix = this.createMatrix(endRow - startRow + 1, endColumn - startColumn + 1);

        for(int i = startRow; i <= endRow; ++i) {
            for(int j = startColumn; j <= endColumn; ++j) {
                subMatrix.setEntry(i - startRow, j - startColumn, this.getEntry(i, j));
            }
        }

        return subMatrix;
    }





    public double[] getRow(int row) throws OutOfRangeException {
        int nCols = this.getColumnDimension();
        double[] out = new double[nCols];

        for(int i = 0; i < nCols; ++i) {
            out[i] = this.getEntry(row, i);
        }

        return out;
    }

    public void setRow(int row, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        int nCols = this.getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        } else {
            for(int i = 0; i < nCols; ++i) {
                this.setEntry(row, i, array[i]);
            }

        }
    }

    public double[] getColumn(int column) throws OutOfRangeException {
        int nRows = this.getRowDimension();
        double[] out = new double[nRows];

        for(int i = 0; i < nRows; ++i) {
            out[i] = this.getEntry(i, column);
        }

        return out;
    }

    public void setColumn(int column, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        int nRows = this.getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        } else {
            for(int i = 0; i < nRows; ++i) {
                this.setEntry(i, column, array[i]);
            }

        }
    }

    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        this.setEntry(row, column, this.getEntry(row, column) + increment);
    }

    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        this.setEntry(row, column, this.getEntry(row, column) * factor);
    }


    public boolean isSquare() {
        return this.getColumnDimension() == this.getRowDimension();
    }

    public abstract int getRowDimension();

    public abstract int getColumnDimension();





    public String toString() {
        StringBuilder res = new StringBuilder();
        String fullClassName = this.getClass().getName();
        String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(46) + 1);
        res.append(shortClassName);

        return res.toString();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof RealMatrix)) {
            return false;
        } else {
            RealMatrix m = (RealMatrix)object;
            int nRows = this.getRowDimension();
            int nCols = this.getColumnDimension();
            if (m.getColumnDimension() == nCols && m.getRowDimension() == nRows) {
                for(int row = 0; row < nRows; ++row) {
                    for(int col = 0; col < nCols; ++col) {
                        try {
                            if (this.getEntry(row, col) != m.getEntry(row, col)) {
                                return false;
                            }
                        } catch (OutOfRangeException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }



    public abstract RealMatrix createMatrix(int var1, int var2) throws NotStrictlyPositiveException;

    public abstract RealMatrix copy() throws Exception;

    public abstract double getEntry(int var1, int var2) throws OutOfRangeException;

    public abstract void setEntry(int var1, int var2, double var3) throws OutOfRangeException;

//    static {
//        DEFAULT_FORMAT = RealMatrixFormat.getInstance(Locale.US);
//        DEFAULT_FORMAT.getFormat().setMinimumFractionDigits(1);
//    }
}