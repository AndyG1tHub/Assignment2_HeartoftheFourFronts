package combat;

import core.GridPosition;

/** Small data class for future area damage effects. */
public class DamageArea {
    private final GridPosition center;
    private final int radius;
    private final int damage;

    public DamageArea(GridPosition center, int radius, int damage) {
        this.center = center;
        this.radius = radius;
        this.damage = damage;
    }

    public boolean contains(GridPosition position) {
        int rowDiff = center.row - position.row;
        int colDiff = center.col - position.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= radius;
    }

    public int getDamage() {
        return damage;
    }
}
