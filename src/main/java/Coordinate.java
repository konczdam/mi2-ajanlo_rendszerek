public class Coordinate {

    private int i,j;

    public Coordinate(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return String.format("(i,j) = (%d,%d)", i,j);
    }
}
