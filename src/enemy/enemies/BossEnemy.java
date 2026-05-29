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

/** Boss enemy that fires purple cross-shaped lasers every 5 seconds,
 *  destroying entire row AND column of towers. */
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
        super.draw(engine, map);
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
}
