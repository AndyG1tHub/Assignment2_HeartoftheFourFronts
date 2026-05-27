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

/** Slow tower with simple area damage. */
public class CannonTower extends AttackTower {
    public CannonTower(GridPosition position) {
        super(position, 150, GameConfig.CANNON_TOWER_COST, 4,
                BuildingType.CANNON_TOWER, 45, 2.4);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && target.getGridPosition().equals(enemy.getGridPosition())) {
                enemy.takeDamage(getDamage());
            }
        }
        projectiles.addProjectile(position, target.getGridPosition(), Color.ORANGE);
        resetCooldown();
        playShootSound();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManger.getAttackTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
            drawHealthBar(engine, map);
            drawLevelIndicator(engine, map);
            return;
        }
        engine.changeColor(new Color(180, 110, 55));
        engine.drawSolidRectangle(x + 4, y + 4, GameConfig.TILE_SIZE - 8, GameConfig.TILE_SIZE - 8);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 8, y + 22, "C", "Arial", 15);
        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
