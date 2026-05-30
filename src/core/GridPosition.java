/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)



 * Immutable row/column coordinate pair for grid tile addressing.
 * Used everywhere — buildings, enemies, pathfinding, and rendering.
 * Immutability makes it safe as a map key without mutation risk.
 * The add() method returns a new instance instead of modifying in place.
 */
package core;

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
