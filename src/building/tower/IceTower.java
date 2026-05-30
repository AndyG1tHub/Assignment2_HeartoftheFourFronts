/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Crowd-control tower unlocked at Level 2.
 * Fires ice projectiles that freeze targets in place for 0.6s on hit.
 * Per-shot damage is low but freeze buys time for other towers.
 * Best paired with high-damage towers to create kill zones.
 * Boss enemies are immune to the freeze effect.
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

/** Low-damage tower that freezes its target in place. */
public class IceTower extends AttackTower {
    public IceTower(GridPosition position) {
        super(position, 120, GameConfig.ICE_TOWER_COST, 4,
                BuildingType.ICE_TOWER, 12, 2.0);
    }

    /** Fires an ice projectile that freezes the target on hit. */
    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }

        int damage = getDamage();

        double freezeDuration = 0.6; // Fixed freeze duration

        // Create projectile with damage and freeze callback
        projectiles.addProjectile(position, target.getGridPosition(), Color.CYAN, () -> {
            if (!target.isDead()) {
                target.takeDamage(damage);
                target.applyFreeze(freezeDuration);
            }
        });

        resetCooldown();
        playShootSound();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManager.getIceTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.3);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
            drawHealthBar(engine, map);
            drawLevelIndicator(engine, map);
            return;
        }
        engine.changeColor(new Color(70, 175, 205));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 11, y + 22, "I", "Arial", 15);
        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
