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
    RealMatrix matrix;

    public Solver(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        initAttributes(scanner);

        matrix = new Array2DRowRealMatrix(numOfUsers,numOfMovies);
        initMatrix(scanner, matrix);


    }

    private void initAttributes(Scanner scanner) {
        numOfRatings = scanner.nextInt();
        numOfUsers = scanner.nextInt();
        numOfMovies = scanner.nextInt();
        users = new ArrayList<>(numOfUsers);
        IntStream.range(0,numOfUsers).forEach(m -> users.add(new User()));
        K = new Double(numOfUsers / 2.5).intValue();
    }

    private void initMatrix(Scanner scanner, RealMatrix matrix) {
        for(int i = 0; i < numOfRatings; ++i){
            int userIndex = scanner.nextInt();
            int movieIndex = scanner.nextInt();
            int rating = scanner.nextInt();
            matrix.setEntry(userIndex, movieIndex, rating);
            users.get(userIndex).addRatedMovie(movieIndex);
        }
    }

    public void solve(){
        RealMatrix P = new Array2DRowRealMatrix(numOfUsers, K);
        RealMatrix Q = new Array2DRowRealMatrix(K, numOfMovies);

        initMatrixWithRandomValues(P);
        initMatrixWithRandomValues(Q);

        System.out.println(P);
        System.out.println(Q);

        
        System.out.println(P.multiply(Q));
    }

    private void initMatrixWithRandomValues(RealMatrix p) {
        int columns = p.getColumnDimension();
        int rows = p.getRowDimension();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                p.setEntry(i,j, ThreadLocalRandom.current().nextInt(5));
            }

        }
    }
}
