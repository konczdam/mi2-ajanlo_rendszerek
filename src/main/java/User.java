import java.util.ArrayList;
import java.util.List;

public class User {

    private List<Integer> ratedMovies = new ArrayList<>();


    public void addRatedMovie(Integer i){
        ratedMovies.add(i);
    }

    public List<Integer> getRatedMovies() {
        return ratedMovies;
    }
}
