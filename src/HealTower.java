import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/** Support building that heals nearby damaged buildings. */
public class HealTower extends Building {
    private static final int HEAL_AMOUNT = 12;
    private static final double HEAL_INTERVAL = 2.0;
    private double cooldown;

    public HealTower(GridPosition position) {
        super(position, 120, GameConfig.HEAL_TOWER_COST, 3, BuildingType.HEAL_TOWER, false);
    }

    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        cooldown = Math.max(0.0, cooldown - dt);
        if (cooldown <= 0.0) {
            healDamagedBuildings(buildings);
            cooldown = HEAL_INTERVAL;
        }
    }

    private void healDamagedBuildings(List<Building> buildings) {
        boolean healedAny = false;
        for (Building building : findDamagedBuildings(buildings)) {
            healBuilding(building);
            healedAny = true;
        }
        if (healedAny) {
            SoundManager sm = SoundManager.getInstance();
            if (sm != null) sm.playHealTower();
        }
    }

    private List<Building> findDamagedBuildings(List<Building> buildings) {
        List<Building> damaged = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building != this && isInHealRange(building) && building.getHp() < building.getMaxHp()) {
                damaged.add(building);
            }
        }
        return damaged;
    }

    private void healBuilding(Building building) {
        building.heal(HEAL_AMOUNT);
    }

    private boolean isInHealRange(Building building) {
        int rowDiff = position.row - building.getPosition().row;
        int colDiff = position.col - building.getPosition().col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= range;
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        engine.changeColor(new Color(95, 205, 165));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 8, y + 22, "H", "Arial", 15);
        drawHealthBar(engine, map);
    }
}
