import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    public Solver(InputStream inputStream)  {
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
        K = Math.max(new Double(numOfUsers / 12.5).intValue(), 3);
        presentValuesInOriginalMatrix = new ArrayList<>(numOfRatings);
    }

    private void initMatrix(Scanner scanner){
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



    private void initMatrixWithRandomValues(RealMatrix p) {
        int columns = p.getColumnDimension();
        int rows = p.getRowDimension();
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                p.setEntry(i,j, tlr.nextInt(4));
    }


    public void solve() {
        RealMatrix P = new Array2DRowRealMatrix(numOfUsers, K);
        RealMatrix Q = new Array2DRowRealMatrix(K, numOfMovies);

        initMatrixWithRandomValues(P);
        initMatrixWithRandomValues(Q);

        int repeats = 1000;
        double alpha = 0.0003, beta = 0.0003;

        for(int repeat = 0; repeat < repeats ; repeat++){
            for(Coordinate c: presentValuesInOriginalMatrix){
                double eij = originalMatrix.getEntry(c.getI(), c.getJ()) - getComputedEntry(P,Q, c);
                for (int k = 0; k <  K; k++) {
                    double newEntry = P.getEntry(c.getI(), k) + alpha * (2 * eij * Q.getEntry(k,c.getJ()) - beta * P.getEntry(c.getI(), k));
                    P.setEntry(c.getI(), k,  newEntry);

                    newEntry = Q.getEntry(k, c.getJ()) + alpha * (2 * eij * P.getEntry(c.getI(), k) - beta * Q.getEntry(k, c.getJ()));
                    Q.setEntry(k, c.getJ(), newEntry);

                }
            }

            RealMatrix candidateMatrix = P.multiply(Q);
            double error = 0;
            for(Coordinate c: presentValuesInOriginalMatrix){
                error += Math.pow( originalMatrix.getEntry(c.getI(), c.getJ()) - candidateMatrix.getEntry(c.getI(), c.getJ()),2);
            }
            if ( error < 0.001){
                break;
            }
        }
        printSuggestions(P.multiply(Q));


    }


    private double getComputedEntry(RealMatrix p, RealMatrix q, Coordinate c) {
        double res = 0;
        for(int i = 0; i < K; i++){
            res += p.getEntry(c.getI(), i) * q.getEntry(i, c.getJ());
        }

        return res;
    }

    private void printSuggestions(RealMatrix matrix ){
        for (int i = 0; i < numOfUsers; i++) {
            double[] ratings = matrix.getRow(i);
            List<Movie> movies = new ArrayList<>(numOfMovies);
            for (int j = 0; j <  ratings.length; j++) {
                movies.add(new Movie(j, ratings[j]));
            }

            Collections.sort(movies);
            int kiiras = 0;
            User actualUser = users.get(i);
            while(kiiras != 10 && !movies.isEmpty()){
                if(!actualUser.getRatedMovies().contains(movies.get(0).getSorszam())){
                    System.out.print(movies.get(0).getSorszam());
                    kiiras++;
                    if(!movies.isEmpty() && kiiras < 10)
                        System.out.print("\t");
                }
                movies.remove(0);
            }
            System.out.print('\n');
        }

    }

}
