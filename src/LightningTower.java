import java.awt.Color;
import java.util.List;

/** Simple multi-target tower used as a placeholder for chain lightning. */
public class LightningTower extends AttackTower {
    public LightningTower(GridPosition position) {
        super(position, 110, GameConfig.LIGHTNING_TOWER_COST, 5,
                BuildingType.LIGHTNING_TOWER, 25, 1.4);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        int hitCount = 0;
        for (Enemy enemy : enemies) {
            if (hitCount >= 2) {
                break;
            }
            if (isEnemyInRange(enemy)) {
                enemy.takeDamage(damage);
                projectiles.addProjectile(position, enemy.getGridPosition(), Color.MAGENTA);
                hitCount++;
            }
        }
        if (hitCount > 0) {
            resetCooldown();
            playShootSound();
        }
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        engine.changeColor(new Color(165, 90, 210));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 9, y + 22, "L", "Arial", 15);
        drawHealthBar(engine, map);
    }
}
