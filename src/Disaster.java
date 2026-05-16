import java.awt.Color;
import java.util.List;

/** Base class for timed natural disasters. */
public abstract class Disaster extends GameEvent {
    protected final GridPosition position;
    protected final int radius;
    protected final int damage;

    protected Disaster(EventType type, GridPosition position, int radius, int damage, double duration) {
        super(type, duration);
        this.position = position;
        this.radius = radius;
        this.damage = damage;
    }

    public abstract void applyEffect(List<Enemy> enemies, List<Building> buildings);

    protected boolean affects(GridPosition other) {
        int rowDiff = position.row - other.row;
        int colDiff = position.col - other.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= radius;
    }

    protected void drawArea(GameEngine engine, GridMap map, Color color) {
        engine.changeColor(color);
        int size = (radius * 2 + 1) * GameConfig.TILE_SIZE;
        int x = map.tileCenterX(position) - size / 2;
        int y = map.tileCenterY(position) - size / 2;
        engine.drawRectangle(x, y, size, size, 2);
    }
}
