/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package core;

import java.util.Objects;

/**
 * <p>An immutable position on the game grid represented by a row and column. Provides utility methods for neighbour calculation and equality checks.</p>
 */
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
