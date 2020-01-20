public class Movie implements  Comparable<Movie> {

    int sorszam;
    double rating;

    public Movie(int sorszam, double rating) {
        this.sorszam = sorszam;
        this.rating = rating;
    }

    public int getSorszam() {
        return sorszam;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public int compareTo(Movie o) {
        double i = rating, value = o.getRating();

        if(value == i)
            return 0;

        return value > i ? 1 : -1;
    }
}