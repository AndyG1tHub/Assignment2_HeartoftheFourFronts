import java.awt.Color;
import java.util.List;

/** Low-damage tower that slows its target. */
public class IceTower extends AttackTower {
    public IceTower(GridPosition position) {
        super(position, 120, GameConfig.ICE_TOWER_COST, 4,
                BuildingType.ICE_TOWER, 10, 1.2);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }
        target.takeDamage(damage);
        target.applySlow(2.0);
        projectiles.addProjectile(position, target.getGridPosition(), Color.CYAN);
        resetCooldown();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        engine.changeColor(new Color(70, 175, 205));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 11, y + 22, "I", "Arial", 15);
        drawHealthBar(engine, map);
    }
}
