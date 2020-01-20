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

    public static final double MAGIC_NUMBER1 = 12.5;
    public static final int MAGIC_NUMBER2 = 3;
    private  int numOfRatings, numOfUsers, numOfMovies, numberOfFeatures;
    private List<User> users;
    private List<Coordinate> presentValuesInOriginalMatrix;
    RealMatrix originalMatrix;
    private RealMatrix p;
    private RealMatrix q;

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
        presentValuesInOriginalMatrix = new ArrayList<>(numOfRatings);

        /**
         *  http://www.quuxlabs.com/blog/2010/09/matrix-factorization-a-simple-tutorial-and-implementation-in-python/
         *  ...we also make the assumption that the number of features would be smaller than the number of users and the number of items....
         */
        numberOfFeatures = Math.max(new Double(numOfUsers / MAGIC_NUMBER1).intValue(), MAGIC_NUMBER2);
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


    public void solveAndPrintSuggestions() {
        p = new Array2DRowRealMatrix(numOfUsers, numberOfFeatures);
        q = new Array2DRowRealMatrix(numberOfFeatures, numOfMovies);

        initMatrixWithRandomValues(p);
        initMatrixWithRandomValues(q);

        int repeats = 1000;
        double alpha = 0.0003, beta = 0.0003;

        for(int repeat = 0; repeat < repeats ; repeat++){
            for(Coordinate c: presentValuesInOriginalMatrix){
                double eij = originalMatrix.getEntry(c.getI(), c.getJ()) - getComputedEntry(p, q, c);
                for (int k = 0; k < numberOfFeatures; k++) {
                    double newEntry = p.getEntry(c.getI(), k) + alpha * (2 * eij * q.getEntry(k,c.getJ()) - beta * p.getEntry(c.getI(), k));
                    p.setEntry(c.getI(), k,  newEntry);

                    newEntry = q.getEntry(k, c.getJ()) + alpha * (2 * eij * p.getEntry(c.getI(), k) - beta * q.getEntry(k, c.getJ()));
                    q.setEntry(k, c.getJ(), newEntry);

                }
            }

            RealMatrix candidateMatrix = p.multiply(q);
            double error = 0;
            for(Coordinate c: presentValuesInOriginalMatrix){
                error += Math.pow( originalMatrix.getEntry(c.getI(), c.getJ()) - candidateMatrix.getEntry(c.getI(), c.getJ()),2);
            }
            if ( error < 0.001){
                break;
            }
        }
        printSuggestions(p.multiply(q));


    }


    private double getComputedEntry(RealMatrix p, RealMatrix q, Coordinate c) {
        double res = 0;
        for(int i = 0; i < numberOfFeatures; i++){
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
            int suggestedMovies = 0;
            User actualUser = users.get(i);
            while(suggestedMovies != 10 && !movies.isEmpty()){
                if(!actualUser.getRatedMovies().contains(movies.get(0).getSorszam())){
                    System.out.print(movies.get(0).getSorszam());
                    suggestedMovies++;
                    if(!movies.isEmpty() && suggestedMovies < 10)
                        System.out.print("\t");
                }
                movies.remove(0);
            }
            System.out.print('\n');
        }

    }

    public void printMatrix() {
        RealMatrix matrix = p.multiply(q);
        System.out.println(matrix.toString());
    }
}
