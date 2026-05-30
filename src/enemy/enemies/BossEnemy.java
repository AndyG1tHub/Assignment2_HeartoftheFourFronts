/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package enemy.enemies;

import java.awt.Color;
import java.util.List;

import building.Building;
import combat.BossLaser;
import core.GridMap;
import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;
import game.GameConfig;
import game.GameEngine;

/**
 * <p>The final boss enemy encountered at stage 5. Possesses very high HP and periodically fires a devastating laser beam across the grid. Defeating the boss triggers victory.</p>
 */
public class BossEnemy extends Enemy {
    public static final int BASE_HP = GameConfig.BOSS_BASE_HP;

    private List<Building> buildings;
    private double laserTimer;
    private BossLaser activeLaser;

    public BossEnemy(GridPosition position, int hp) {
        super(position, EnemyType.BOSS, hp, GameConfig.BOSS_DAMAGE,
                GameConfig.BOSS_SPEED, 60, 300);
        laserTimer = BossLaser.FIRE_INTERVAL * 0.5; // first laser fires after 2.5s
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public double getBaseAttackRange() {
        return 2.0;
    }

    @Override
    public void update(double dt, enemy.EnemyAI ai) {
        super.update(dt, ai);
        if (isFrozen()) {
            return;
        }
        updateLaser(dt);
    }

    private void updateLaser(double dt) {
        if (activeLaser != null && activeLaser.isActive()) {
            activeLaser.update(dt);
            if (!activeLaser.isActive()) {
                activeLaser = null;
            }
            return;
        }
        laserTimer -= dt;
        if (laserTimer <= 0 && buildings != null && !buildings.isEmpty()) {
            activeLaser = new BossLaser(getGridPosition(), buildings);
            setAttackAnimationTimer(BossLaser.TOTAL_DURATION);
            laserTimer = BossLaser.FIRE_INTERVAL;
        }
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        draw(engine, map, 0.0, 0.0);
    }

    @Override
    public void draw(GameEngine engine, GridMap map, double offsetX, double offsetY) {
        super.draw(engine, map, offsetX, offsetY);
        if (activeLaser != null && activeLaser.isActive()) {
            activeLaser.draw(engine);
        }
    }

    @Override
    protected void onAttackLands() {
        // Laser is fired on timer, not on attack
    }

    @Override
    protected double getDrawSizeMultiplier() {
        return 1.85;
    }

    @Override
    protected boolean shouldSnapOnDamageAnimation() {
        return false;
    }

    @Override
    protected Color getColor() {
        return new Color(120, 50, 175);
    }

    @Override
    protected String getDrawLabel() {
        return "B";
    }

    @Override
    public void applyFreeze(double duration) {
        // Boss is immune to freeze effects
    }
}
