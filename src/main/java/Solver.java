
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;



public class Solver {

    private  int numOfRatings, numOfUsers, numOfMovies, K;
    private List<User> users;
    private List<Coordinate> presentValuesInOriginalMatrix;
    RealMatrix originalMatrix;
    private final double epsilon = 0.25;
    private static final double DOUBLE_COMPARE_EPSILON = 0.0001;
    private double acceptedTotalError;
    private RealMatrix canditateMatrix;
    boolean preciseMode = false;

    public Solver(InputStream inputStream) throws OutOfRangeException, NotStrictlyPositiveException {
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
        acceptedTotalError = numOfRatings * 0.5;
        presentValuesInOriginalMatrix = new ArrayList<>(numOfRatings);
    }

    private void initMatrix(Scanner scanner) throws NotStrictlyPositiveException, OutOfRangeException {
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

    public void solve() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        RealMatrix P = new Array2DRowRealMatrix(numOfUsers, K);
        RealMatrix Q = new Array2DRowRealMatrix(K, numOfMovies);

        initMatrixWithRandomValues(P);
        initMatrixWithRandomValues(Q);
        double Totalerror = 0;
        while(true){
            canditateMatrix = P.multiply(Q);
            double newTotalError = getTotalError();
            if(!preciseMode && Math.abs(newTotalError - Totalerror) < 2){
                preciseMode = true;

            }
            Totalerror = newTotalError;
            System.out.println("Totalerror: " + Totalerror);
            if(!isTotalErrorTooBig())
                break;
            editMatrices(P,Q);
        }

        System.out.println(P);
        System.out.println(Q);


        System.out.println(P.multiply(Q));
    }

    private void initMatrixWithRandomValues(RealMatrix p) throws OutOfRangeException {
        int columns = p.getColumnDimension();
        int rows = p.getRowDimension();
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                p.setEntry(i,j, tlr.nextInt(4));
    }

    private boolean isTotalErrorTooBig() throws OutOfRangeException {
        return getTotalError() > acceptedTotalError;
    }

    private double getTotalError() throws OutOfRangeException {
        double res = 0.0;
        for(Coordinate c: presentValuesInOriginalMatrix){
            double entry = originalMatrix.getEntry(c.getI(), c.getJ());
            res += Math.pow(entry - canditateMatrix.getEntry(c.getI(),c.getJ()) , 2);
        }

        return res;

    }

    double precision = 1;

    private void editMatrices(RealMatrix P, RealMatrix Q) throws OutOfRangeException {
        if(!preciseMode){
            for(Coordinate c: presentValuesInOriginalMatrix){
                double diff = originalMatrix.getEntry(c.getI(), c.getJ()) - canditateMatrix.getEntry(c.getI(), c.getJ());

                if(Math.abs(diff) > epsilon){
                    editRowOfMatrix(P, c.getI(), diff * 0.1);
                    editColumnOfMatrix(Q, c.getJ(), diff * 0.1 ) ;

                }
            }
        }
        else{
            while(true){
            Coordinate c =presentValuesInOriginalMatrix.get(ThreadLocalRandom.current().nextInt(presentValuesInOriginalMatrix.size()));
            double diff = originalMatrix.getEntry(c.getI(), c.getJ()) - canditateMatrix.getEntry(c.getI(), c.getJ());

            if(Math.abs(diff) > epsilon){
                editRowOfMatrix(P, c.getI(), diff * 0.01);
                editColumnOfMatrix(Q, c.getJ(), diff * 0.01 ) ;
                break;
            }

            }

        }

    }


    private void editRowOfMatrix(RealMatrix p, int rowIndex, double v) throws OutOfRangeException {
        int  columns = p.getColumnDimension();
        for(int i = 0; i < columns; i++){
            p.setEntry(rowIndex, i, Math.max(p.getEntry(rowIndex, i) + v, 0));
        }
    }

    private void editColumnOfMatrix(RealMatrix q, int columnIndex, double v) throws OutOfRangeException {
        int rows = q.getRowDimension();
        for (int i = 0; i < rows ; i++) {
            q.setEntry(i, columnIndex, Math.max(q.getEntry(i, columnIndex) + v,0));
        }
    }


    public void solve2() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        RealMatrix P = new Array2DRowRealMatrix(numOfUsers, K);
        RealMatrix Q = new Array2DRowRealMatrix(K, numOfMovies);

        initMatrixWithRandomValues(P);
        initMatrixWithRandomValues(Q);

        int steps = 5000;
        double alpha = 0.0002;

        for(int step = 0; step < 5000 ; step++){
            for(Coordinate c: presentValuesInOriginalMatrix){
                double eij = originalMatrix.getEntry(c.getI(), c.getJ()) - getComputedEntry(P,Q, c);
                for (int k = 0; k <  K; k++) {
                    double newEntry = P.getEntry(c.getI(), k) + alpha * (2 * eij * Q.getEntry(k,c.getJ()) - alpha * P.getEntry(c.getI(), k));
                    P.setEntry(c.getI(), k,  newEntry);

                    newEntry = Q.getEntry(k, c.getJ()) + alpha * (2 * eij * P.getEntry(c.getI(), k) - alpha * Q.getEntry(k, c.getJ()));
                    Q.setEntry(k, c.getJ(), newEntry);
                }
             }

            RealMatrix candidateMatrix = P.multiply(Q);
            double e = 0;
            for(Coordinate c: presentValuesInOriginalMatrix){
                e += Math.pow( originalMatrix.getEntry(c.getI(), c.getJ()) - candidateMatrix.getEntry(c.getI(), c.getJ()),2);
            }
            if ( e < 0.001){
                System.out.println(step);
                break;
            }
        }

        printSuggestions(P.multiply(Q));


    }


    private double getComputedEntry(RealMatrix p, RealMatrix q, Coordinate c) throws OutOfRangeException {
        double res = 0;
        for(int i = 0; i < K; i++){
            res += p.getEntry(c.getI(), i) * q.getEntry(i, c.getJ());
        }

        return res;
    }

    private void printSuggestions(RealMatrix matrix ) throws OutOfRangeException {
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
            System.out.println('\n');
        }

    }

}
