package combat;

import java.awt.Color;
import java.awt.Image;

import core.GridPosition;
import core.GridMap;
import game.GameConfig;
import game.GameEngine;
import manager.ImageManger;

/** Lightweight visual projectile. Damage is applied immediately by towers for now. */
public class Projectile {
    private final GridPosition start;
    private final GridPosition target;
    private final Color color;
    private double life;

    public Projectile(GridPosition start, GridPosition target, Color color) {
        this.start = start;
        this.target = target;
        this.color = color;
        this.life = 0.18;
    }

    public void update(double dt) {
        life -= dt;
    }

    public boolean isFinished() {
        return life <= 0.0;
    }

    public void draw(GameEngine engine, GridMap map) {
        double progress = Math.max(0.0, Math.min(1.0, 1.0 - life / 0.18));
        double x = lerp(map.tileCenterX(start), map.tileCenterX(target), progress);
        double y = lerp(map.tileCenterY(start), map.tileCenterY(target), progress);
        Image image = getProjectileImage();
        if (image != null) {
            double size = color == Color.MAGENTA ? GameConfig.TILE_SIZE * 1.6 : GameConfig.TILE_SIZE;
            if (color == Color.YELLOW) {
                drawArrow(engine, image, x, y, size, map);
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
            return ImageManger.getArrowProjectile();
        }
        if (color == Color.ORANGE) {
            return ImageManger.getCannonProjectile();
        }
        if (color == Color.CYAN) {
            return ImageManger.getIceProjectile();
        }
        if (color == Color.MAGENTA) {
            return ImageManger.getLaserEffect();
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

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
