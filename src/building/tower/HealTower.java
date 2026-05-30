/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Support building unlocked at Level 4 that heals nearby attack towers.
 * Continuously heals up to three damaged towers at 15 HP/second each.
 * Healing beams render as pulsing green lasers with energy particles.
 * Has no projectile interaction and contributes no direct damage.
 * Extends defence line survivability during boss fights and elite waves.
 */
package building.tower;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

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

/** Support building that heals nearby damaged towers with green laser beams. */
public class HealTower extends Building {
    private static final double HEAL_INTERVAL = 0.5;
    private static final int MAX_HEAL_TARGETS = 3;
    private static final double HEAL_PER_SECOND = 15.0;

    private double cooldown;
    private double beamAnimationTime = 0.0;
    private List<Building> currentTargets = new ArrayList<Building>();
    private double accumulatedHeal = 0.0;

    public HealTower(GridPosition position) {
        super(position, 120, GameConfig.HEAL_TOWER_COST, 2, BuildingType.HEAL_TOWER);
    }

    private int getHealAmount() {
        return GameConfig.HEAL_AMOUNT_UPGRADE[upgradeLevel];
    }

    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        beamAnimationTime += dt;
        cooldown = Math.max(0.0, cooldown - dt);

        // Update current targets - remove destroyed, fully healed or out of range towers
        currentTargets.removeIf(building ->
            building.isDestroyed() ||
            building.getHp() >= building.getMaxHp() ||
            !isInHealRange(building));

        // Find new targets if we have slots available
        if (currentTargets.size() < MAX_HEAL_TARGETS) {
            List<Building> damagedTowers = findDamagedTowers(buildings);
            for (Building tower : damagedTowers) {
                if (!currentTargets.contains(tower) && currentTargets.size() < MAX_HEAL_TARGETS) {
                    currentTargets.add(tower);
                }
            }
        }

        // Heal current targets continuously
        if (!currentTargets.isEmpty()) {
            double healMultiplier = GameConfig.DAMAGE_MULTIPLIER[upgradeLevel];
            double healThisFrame = HEAL_PER_SECOND * healMultiplier * dt;
            accumulatedHeal += healThisFrame;

            if (accumulatedHeal >= 1.0) {
                int healToApply = (int) accumulatedHeal;
                accumulatedHeal -= healToApply;

                for (Building target : currentTargets) {
                    target.heal(healToApply);
                }

                if (cooldown <= 0.0) {
                    SoundManager sm = SoundManager.getInstance();
                    if (sm != null) sm.playHealTower();
                    cooldown = HEAL_INTERVAL;
                }
            }
        } else {
            accumulatedHeal = 0.0;
        }
    }

    private List<Building> findDamagedTowers(List<Building> buildings) {
        List<Building> damaged = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building != this &&
                isTower(building) &&
                isInHealRange(building) &&
                building.getHp() < building.getMaxHp()) {
                damaged.add(building);
            }
        }
        return damaged;
    }

    private boolean isTower(Building building) {
        BuildingType type = building.getType();
        return type == BuildingType.ARROW_TOWER ||
               type == BuildingType.CANNON_TOWER ||
               type == BuildingType.ICE_TOWER ||
               type == BuildingType.LIGHTNING_TOWER;
    }

    private boolean isInHealRange(Building building) {
        int rowDiff = position.row - building.getPosition().row;
        int colDiff = position.col - building.getPosition().col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= range;
    }

    public void drawRangeEffect(GameEngine engine, GridMap map) {
        Image rangeImage = ImageManager.getHealRangeEffect();
        if (rangeImage != null) {
            int size = (range * 2 + 1) * GameConfig.TILE_SIZE;
            engine.setAlpha(0.5f);
            engine.drawImage(rangeImage, map.tileCenterX(position) - size / 2,
                    map.tileCenterY(position) - size / 2, size, size);
            engine.setAlpha(1.0f);
        }
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManager.getHealTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
        } else {
            engine.changeColor(new Color(95, 205, 165));
            engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
            engine.changeColor(Color.WHITE);
            engine.drawText(x + 8, y + 22, "H", "Arial", 15);
        }

        // Draw green laser beams to current targets
        for (Building target : currentTargets) {
            int centerX = map.tileCenterX(position);
            int centerY = map.tileCenterY(position);
            int targetX = map.tileCenterX(target.getPosition());
            int targetY = map.tileCenterY(target.getPosition());

            // Pulsing effect
            double pulse = Math.sin(beamAnimationTime * 8.0) * 0.3 + 1.0;

            // Outer glow (light green) - 50% transparency
            engine.changeColor(new Color(150, 255, 150, 127));
            engine.drawLine(centerX, centerY, targetX, targetY, 12.0 * pulse);

            // Middle layer (bright green) - 50% transparency
            engine.changeColor(new Color(100, 255, 100, 127));
            engine.drawLine(centerX, centerY, targetX, targetY, 7.0 * pulse);

            // Core beam (bright white-green) - 50% transparency
            engine.changeColor(new Color(180, 255, 180, 127));
            engine.drawLine(centerX, centerY, targetX, targetY, 3.5 * pulse);

            // Draw energy particles along the beam - 50% transparency
            double distance = Math.sqrt(Math.pow(targetX - centerX, 2) + Math.pow(targetY - centerY, 2));
            int particleCount = (int)(distance / 12);
            for (int i = 0; i < particleCount; i++) {
                double t = (i / (double)particleCount + beamAnimationTime * 0.5) % 1.0;
                double px = centerX + (targetX - centerX) * t;
                double py = centerY + (targetY - centerY) * t;
                double particleSize = 4 + Math.sin(beamAnimationTime * 10 + i) * 2;

                engine.changeColor(new Color(150, 255, 150, 127));
                engine.drawSolidCircle(px, py, particleSize);
            }
        }

        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
