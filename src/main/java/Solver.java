import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;


public class Solver {

    private  int numOfRatings, numOfUsers, numOfMovies, K;
    private List<User> users;
    private List<Coordinate> presentValuesInOriginalMatrix;
    RealMatrix originalMatrix;
    private final double epsilon = 0.25;
    private static final double DOUBLE_COMPARE_EPSILON = 0.0001;
    private double acceptedTotalError;
    private RealMatrix canditateMatrix;

    public Solver(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        initAttributes(scanner);
        initMatrix(scanner);
    }

    private void initAttributes(Scanner scanner) {
        numOfRatings = scanner.nextInt();
        numOfUsers = scanner.nextInt();
        numOfMovies = scanner.nextInt();
        users = new ArrayList<>(numOfUsers);
        IntStream.range(0,numOfUsers).forEach(m -> users.add(new User()));
        K = new Double(numOfUsers / 2.5).intValue();
        acceptedTotalError = numOfRatings * 0.951;
        presentValuesInOriginalMatrix = new ArrayList<>(numOfRatings);
    }

    private void initMatrix(Scanner scanner) {
        originalMatrix = new Array2DRowRealMatrix(numOfUsers,numOfMovies);
        for(int i = 0; i < numOfRatings; ++i){
            int userIndex = scanner.nextInt();
            int movieIndex = scanner.nextInt();
            int rating = scanner.nextInt();
            originalMatrix.setEntry(userIndex, movieIndex, rating);
            users.get(userIndex).addRatedMovie(movieIndex);
            presentValuesInOriginalMatrix.add(new Coordinate(userIndex, movieIndex));
        }
    }

    public void solve(){
        RealMatrix P = new Array2DRowRealMatrix(numOfUsers, K);
        RealMatrix Q = new Array2DRowRealMatrix(K, numOfMovies);

        initMatrixWithRandomValues(P);
        initMatrixWithRandomValues(Q);
        System.out.println(originalMatrix);

        while(true){
            canditateMatrix = P.multiply(Q);
            if(!isTotalErrorTooBig())
                break;
            editMatrices(P,Q);
        }

        System.out.println(P);
        System.out.println(Q);


        System.out.println(P.multiply(Q));
    }

    private void initMatrixWithRandomValues(RealMatrix p) {
        int columns = p.getColumnDimension();
        int rows = p.getRowDimension();
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                p.setEntry(i,j, tlr.nextInt(5));
    }

    private boolean isTotalErrorTooBig() {
        return getTotalError() > acceptedTotalError;
    }

    private double getTotalError() {
        double res = 0.0;
        for(Coordinate c: presentValuesInOriginalMatrix){
            double entry = originalMatrix.getEntry(c.getI(), c.getJ());
            res += Math.pow(entry - canditateMatrix.getEntry(c.getI(),c.getJ()) , 2);
        }

        return res;

    }

    private void editMatrices(RealMatrix P, RealMatrix Q) {
        K:for(Coordinate c: presentValuesInOriginalMatrix){
            double diff = originalMatrix.getEntry(c.getI(), c.getJ()) - canditateMatrix.getEntry(c.getI(), c.getJ());

            if(Math.abs(diff) > epsilon){
                editRowOfMatrix(P, c.getI(), diff * 0.1);
                editColumnOfMatrix(Q, c.getJ(), diff * 0.1);
                break K;
            }
        }

    }


    private void editRowOfMatrix(RealMatrix p, int rowIndex, double v) {
        int  columns = p.getColumnDimension();
        for(int i = 0; i < columns; i++){
            p.setEntry(rowIndex, i, p.getEntry(rowIndex, i) + v);
        }
    }

    private void editColumnOfMatrix(RealMatrix q, int columnIndex, double v) {
        int rows = q.getRowDimension();
        for (int i = 0; i < rows ; i++) {
            q.setEntry(i, columnIndex, q.getEntry(i, columnIndex) + v);
        }
    }


}
