import java.util.List;

/** Base class for all one-tile player buildings. */
public abstract class Building {
    protected final GridPosition position;
    protected int hp;
    protected final int maxHp;
    protected final int cost;
    protected final int range;
    protected final BuildingType type;
    protected final boolean blocksPath;

    protected Building(GridPosition position, int maxHp, int cost, int range,
            BuildingType type, boolean blocksPath) {
        this.position = position;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.cost = cost;
        this.range = range;
        this.type = type;
        this.blocksPath = blocksPath;
    }

    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        // Default buildings do nothing each frame.
    }

    public abstract void draw(GameEngine engine, GridMap map);

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public GridPosition getPosition() {
        return position;
    }

    public int getCost() {
        return cost;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getRange() {
        return range;
    }

    public BuildingType getType() {
        return type;
    }

    public boolean blocksPath() {
        return blocksPath;
    }

    protected void drawHealthBar(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col) + 4;
        int y = map.toScreenY(position.row) + GameConfig.TILE_SIZE - 6;
        double ratio = maxHp == 0 ? 0.0 : (double) hp / maxHp;
        engine.changeColor(java.awt.Color.RED);
        engine.drawSolidRectangle(x, y, GameConfig.TILE_SIZE - 8, 3);
        engine.changeColor(java.awt.Color.GREEN);
        engine.drawSolidRectangle(x, y, (GameConfig.TILE_SIZE - 8) * ratio, 3);
    }
}
