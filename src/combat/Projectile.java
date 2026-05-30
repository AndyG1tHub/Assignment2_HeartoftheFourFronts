/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Lightweight visual projectile that travels from tower to target.
 * Damage is applied via a Runnable callback when the projectile arrives.
 * Arrow, cannon, and ice types each have distinct speeds and rotations.
 * Arrows rotate to face flight direction; cannon/ice tails point backward.
 */
package combat;

import java.awt.Color;
import java.awt.Image;

import core.GridPosition;
import core.GridMap;
import game.GameConfig;
import game.GameEngine;
import manager.ImageManager;

/** Lightweight visual projectile. Damage is applied when projectile reaches target. */
public class Projectile {
    private final GridPosition start;
    private final GridPosition target;
    private final Color color;
    private double life;
    private final Runnable onHitCallback;
    private boolean hasHit = false;

    public Projectile(GridPosition start, GridPosition target, Color color) {
        this(start, target, color, null);
    }

    public Projectile(GridPosition start, GridPosition target, Color color, Runnable onHitCallback) {
        this.start = start;
        this.target = target;
        this.color = color;
        this.onHitCallback = onHitCallback;
        // Different projectile speeds for visibility
        if (color == Color.YELLOW) {
            this.life = 0.4;  // Arrow
        } else if (color == Color.ORANGE) {
            this.life = 0.5;  // Cannon
        } else if (color == Color.CYAN) {
            this.life = 0.4; // Ice
        } else {
            this.life = 0.18; // Default
        }
    }

    public void update(double dt) {
        life -= dt;
        // Trigger callback when projectile hits (just before finishing)
        if (!hasHit && life <= 0.0 && onHitCallback != null) {
            onHitCallback.run();
            hasHit = true;
        }
    }

    public boolean isFinished() {
        return life <= 0.0;
    }

    public void draw(GameEngine engine, GridMap map) {
        double maxLife;
        if (color == Color.YELLOW) {
            maxLife = 0.4;
        } else if (color == Color.ORANGE) {
            maxLife = 0.5;
        } else if (color == Color.CYAN) {
            maxLife = 0.4;
        } else {
            maxLife = 0.18;
        }
        double progress = Math.max(0.0, Math.min(1.0, 1.0 - life / maxLife));
        double x = lerp(map.tileCenterX(start), map.tileCenterX(target), progress);
        double y = lerp(map.tileCenterY(start), map.tileCenterY(target), progress);
        Image image = getProjectileImage();
        if (image != null) {
            double size = color == Color.MAGENTA ? GameConfig.TILE_SIZE * 1.6 : GameConfig.TILE_SIZE;
            if (color == Color.YELLOW) {
                drawArrow(engine, image, x, y, size, map);
            } else if (color == Color.ORANGE || color == Color.CYAN) {
                // Rotate cannon and ice projectiles based on direction
                drawRotatedProjectile(engine, image, x, y, size, map);
            } else {
                engine.drawImage(image, x - size / 2, y - size / 2, size, size);
            }
            return;
        }
        engine.changeColor(color);
        engine.drawSolidCircle(x, y, 4);
    }

    private Image getProjectileImage() {
        if (color == Color.YELLOW) {
            return ImageManager.getArrowProjectile();
        }
        if (color == Color.ORANGE) {
            return ImageManager.getCannonProjectile();
        }
        if (color == Color.CYAN) {
            return ImageManager.getIceProjectile();
        }
        if (color == Color.MAGENTA) {
            return ImageManager.getLaserEffect();
        }
        return null;
    }

    private void drawArrow(GameEngine engine, Image image, double x, double y, double size, GridMap map) {
        double dx = map.tileCenterX(target) - map.tileCenterX(start);
        double dy = map.tileCenterY(target) - map.tileCenterY(start);
        double angle = Math.toDegrees(Math.atan2(dy, dx)) + 45.0;
        engine.saveCurrentTransform();
        engine.translate(x, y);
        engine.rotate(angle);
        engine.drawImage(image, -size / 2, -size / 2, size, size);
        engine.restoreLastTransform();
    }

    private void drawRotatedProjectile(GameEngine engine, Image image, double x, double y, double size, GridMap map) {
        // Calculate direction from current position back to start (tower)
        // This makes the tail point toward the tower
        double dx = map.tileCenterX(start) - x;
        double dy = map.tileCenterY(start) - y;
        // Add 45 degrees because default image has tail at top-right (45 degrees)
        double angle = Math.toDegrees(Math.atan2(dy, dx)) + 45.0;
        engine.saveCurrentTransform();
        engine.translate(x, y);
        engine.rotate(angle);
        engine.drawImage(image, -size / 2, -size / 2, size, size);
        engine.restoreLastTransform();
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
