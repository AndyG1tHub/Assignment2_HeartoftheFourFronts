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

/** Low-damage tower that slows its target. */
public class IceTower extends AttackTower {
    public IceTower(GridPosition position) {
        super(position, 120, GameConfig.ICE_TOWER_COST, 4,
                BuildingType.ICE_TOWER, 18, 2.0);
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }

        int damage = getDamage();

        // Create projectile with damage and slow callback
        projectiles.addProjectile(position, target.getGridPosition(), Color.CYAN, () -> {
            if (!target.isDead()) {
                target.takeDamage(damage);
                target.applySlow(2.0);
            }
        });

        resetCooldown();
        playShootSound();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManger.getIceTower();
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
