/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package building.tower;

import java.awt.Color;
import java.util.List;

import building.Building;
import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import game.GameConfig;
import enemy.Enemy;
import combat.ProjectileManager;
import manager.SoundManager;

/** Shared targeting and cooldown logic for attack towers. */
public abstract class AttackTower extends Building {
    protected final int baseDamage;
    protected final double baseAttackInterval;
    protected final int baseRange;
    protected double cooldown;
    protected double disabledTimer;

    protected AttackTower(GridPosition position, int maxHp, int cost, int range,
            BuildingType type, int damage, double attackInterval) {
        super(position, maxHp, cost, range, type);
        this.baseDamage = damage;
        this.baseAttackInterval = attackInterval;
        this.baseRange = range;
    }

    public int getDamage() {
        return (int) (baseDamage * GameConfig.DAMAGE_MULTIPLIER[upgradeLevel]);
    }

    @Override
    public int getRange() {
        return baseRange + GameConfig.RANGE_BONUS[upgradeLevel];
    }

    public double getAttackInterval() {
        return baseAttackInterval * GameConfig.ATTACK_INTERVAL_MULTIPLIER[upgradeLevel];
    }

    /** Ticks cooldowns and fires when a target is available. */
    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        cooldown = Math.max(0.0, cooldown - dt);
        disabledTimer = Math.max(0.0, disabledTimer - dt);
        if (disabledTimer > 0.0) {
            return;
        }
        if (canAttack()) {
            attack(enemies, projectiles, map);
        }
    }

    /** Fires a projectile at the closest enemy in range. */
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }

        // Create projectile with damage callback
        projectiles.addProjectile(position, target.getGridPosition(), Color.YELLOW, () -> {
            if (!target.isDead()) {
                target.takeDamage(getDamage());
            }
        });

        resetCooldown();
        playShootSound();
    }

    protected void playShootSound() {
        SoundManager sm = SoundManager.getInstance();
        if (sm == null) return;
        switch (getType()) {
            case ARROW_TOWER: sm.playArrowShoot(); break;
            case CANNON_TOWER: sm.playCannonShoot(); break;
            case ICE_TOWER: sm.playIceShoot(); break;
            case LIGHTNING_TOWER: sm.playLightningShoot(); break;
        }
    }

    /** Finds the nearest living enemy within tower range. */
    protected Enemy findClosestEnemy(List<Enemy> enemies) {
        Enemy closest = null;
        double bestDistance = Double.MAX_VALUE;
        int currentRange = getRange();
        for (Enemy enemy : enemies) {
            double distance = distanceTo(enemy.getGridPosition());
            if (!enemy.isDead() && distance <= currentRange && distance < bestDistance) {
                closest = enemy;
                bestDistance = distance;
            }
        }
        return closest;
    }

    protected boolean isEnemyInRange(Enemy enemy) {
        return !enemy.isDead() && distanceTo(enemy.getGridPosition()) <= getRange();
    }

    protected double distanceTo(GridPosition other) {
        int rowDiff = position.row - other.row;
        int colDiff = position.col - other.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }

    protected boolean canAttack() {
        return cooldown <= 0.0;
    }

    public void disable(double duration) {
        disabledTimer = Math.max(disabledTimer, duration);
    }

    protected void resetCooldown() {
        cooldown = getAttackInterval();
    }
}
