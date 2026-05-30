/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)



 * Area-damage tower unlocked at Level 3.
 * Fires cannonballs that deal splash damage to all enemies on the target tile.
 * Attacks slowly (3.0s cooldown) but hits hard.
 * Ideal for clustered groups and tank enemies.
 * Range is 3 tiles, shorter than ArrowTower.
 */
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
import manager.ImageManager;

/** Slow tower with simple area damage. */
public class CannonTower extends AttackTower {
    public CannonTower(GridPosition position) {
        super(position, 150, GameConfig.CANNON_TOWER_COST, 3,
                BuildingType.CANNON_TOWER, 50, 3.0);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }

        GridPosition targetPos = target.getGridPosition();
        int damage = getDamage();

        // Create projectile with damage callback
        projectiles.addProjectile(position, targetPos, Color.ORANGE, () -> {
            // Deal damage to all enemies in the same tile when projectile hits
            for (Enemy enemy : enemies) {
                if (!enemy.isDead() && enemy.getGridPosition().equals(targetPos)) {
                    enemy.takeDamage(damage);
                }
            }
        });

        resetCooldown();
        playShootSound();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManager.getAttackTower();
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
