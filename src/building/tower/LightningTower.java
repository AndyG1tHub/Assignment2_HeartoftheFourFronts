package building.tower;

import java.awt.Color;
import java.awt.Image;
import java.util.List;
import java.util.ArrayList;

import building.Building;
import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import combat.ProjectileManager;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManager;
import manager.SoundManager;

/** Laser tower with 3x3 range and continuous beam damage. */
public class LightningTower extends AttackTower {
    private static final double DAMAGE_PER_SECOND = 20.0;
    private static final double DAMAGE_RAMP_UP_RATE = 5.0; // Additional damage per second of continuous fire
    private static final double MAX_DAMAGE_MULTIPLIER = 3.0; // Max 3x damage
    private Enemy currentTarget = null;
    private double accumulatedDamage = 0.0;
    private double beamAnimationTime = 0.0;
    private double lockOnTime = 0.0;
    private boolean isFiring = false;

    public LightningTower(GridPosition position) {
        super(position, 110, GameConfig.LIGHTNING_TOWER_COST, 2,
                BuildingType.LIGHTNING_TOWER, 25, 1.0);
    }

    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {

        beamAnimationTime += dt;

        // Check if current target is still valid
        if (currentTarget != null) {
            if (currentTarget.isDead() || !isInLaserRange(currentTarget)) {
                // Target lost, reset
                currentTarget = null;
                lockOnTime = 0.0;
                accumulatedDamage = 0.0;

                // Stop sound when losing target
                if (isFiring) {
                    SoundManager sm = SoundManager.getInstance();
                    if (sm != null) sm.stopLightningLoop();
                    isFiring = false;
                }
            }
        }

        // Only find new target if we don't have one
        if (currentTarget == null) {
            double closestDistance = Double.MAX_VALUE;

            for (Enemy enemy : enemies) {
                if (isInLaserRange(enemy)) {
                    double distance = getDistanceTo(enemy.getGridPosition());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        currentTarget = enemy;
                    }
                }
            }

            if (currentTarget != null) {
                lockOnTime = 0.0;

                // Start sound when acquiring target
                if (!isFiring) {
                    SoundManager sm = SoundManager.getInstance();
                    if (sm != null) sm.startLightningLoop();
                    isFiring = true;
                }
            }
        }

        if (currentTarget == null) {
            accumulatedDamage = 0.0;
            return;
        }

        // Increase lock-on time for damage ramp-up
        lockOnTime += dt;

        // Calculate ramping damage
        double damageMultiplier = GameConfig.DAMAGE_MULTIPLIER[upgradeLevel];
        double rampMultiplier = Math.min(1.0 + (lockOnTime * DAMAGE_RAMP_UP_RATE / DAMAGE_PER_SECOND), MAX_DAMAGE_MULTIPLIER);
        double damageThisFrame = DAMAGE_PER_SECOND * damageMultiplier * rampMultiplier * dt;

        accumulatedDamage += damageThisFrame;

        if (accumulatedDamage >= 1.0) {
            int damageToApply = (int) accumulatedDamage;
            accumulatedDamage -= damageToApply;
            currentTarget.takeDamage(damageToApply);
        }
    }

    @Override
    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManager.getLightningTower();

        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
        } else {
            engine.changeColor(new Color(165, 90, 210));
            engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
            engine.changeColor(Color.WHITE);
            engine.drawText(x + 9, y + 22, "L", "Arial", 15);
        }

        // Draw laser beam to current target
        if (currentTarget != null) {
            int centerX = map.tileCenterX(position);
            int centerY = map.tileCenterY(position);
            int targetX = map.tileCenterX(currentTarget.getGridPosition());
            int targetY = map.tileCenterY(currentTarget.getGridPosition());

            // Pulsing effect
            double pulse = Math.sin(beamAnimationTime * 8.0) * 0.3 + 1.0;

            // Outer glow (light purple)
            engine.changeColor(new Color(200, 150, 255, 60));
            engine.drawLine(centerX, centerY, targetX, targetY, 12.0 * pulse);

            // Middle layer (bright purple)
            engine.changeColor(new Color(160, 100, 255, 150));
            engine.drawLine(centerX, centerY, targetX, targetY, 7.0 * pulse);

            // Core beam (bright white-purple)
            engine.changeColor(new Color(220, 180, 255, 255));
            engine.drawLine(centerX, centerY, targetX, targetY, 3.5 * pulse);

            // Draw energy particles along the beam
            double distance = Math.sqrt(Math.pow(targetX - centerX, 2) + Math.pow(targetY - centerY, 2));
            int particleCount = (int)(distance / 12);
            for (int i = 0; i < particleCount; i++) {
                double t = (i / (double)particleCount + beamAnimationTime * 0.5) % 1.0;
                double px = centerX + (targetX - centerX) * t;
                double py = centerY + (targetY - centerY) * t;
                double particleSize = 4 + Math.sin(beamAnimationTime * 10 + i) * 2;

                engine.changeColor(new Color(200, 150, 255, 220));
                engine.drawSolidCircle(px, py, particleSize);
            }
        }

        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }

    private boolean isInLaserRange(Enemy enemy) {
        if (enemy.isDead()) {
            return false;
        }
        int rowDiff = Math.abs(position.row - enemy.getGridPosition().row);
        int colDiff = Math.abs(position.col - enemy.getGridPosition().col);
        return rowDiff <= 1 && colDiff <= 1;
    }

    private double getDistanceTo(GridPosition other) {
        int rowDiff = position.row - other.row;
        int colDiff = position.col - other.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }
}
