package building.tower;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import combat.ProjectileManager;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManger;

/** Simple multi-target tower used as a placeholder for chain lightning. */
public class LightningTower extends AttackTower {
    public LightningTower(GridPosition position) {
        super(position, 110, GameConfig.LIGHTNING_TOWER_COST, 5,
                BuildingType.LIGHTNING_TOWER, 28, 2.2);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        int hitCount = 0;
        for (Enemy enemy : enemies) {
            if (hitCount >= 2) {
                break;
            }
            if (isEnemyInRange(enemy)) {
                enemy.takeDamage(getDamage());
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
        Image image = ImageManger.getLightningTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
            drawHealthBar(engine, map);
            drawLevelIndicator(engine, map);
            return;
        }
        engine.changeColor(new Color(165, 90, 210));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 9, y + 22, "L", "Arial", 15);
        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
