import java.util.Objects;

/** Immutable row/column coordinate for the grid map. */
public class GridPosition {
    public final int row;
    public final int col;

    public GridPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public GridPosition add(int rowDelta, int colDelta) {
        return new GridPosition(row + rowDelta, col + colDelta);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GridPosition)) {
            return false;
        }
        GridPosition that = (GridPosition) other;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
