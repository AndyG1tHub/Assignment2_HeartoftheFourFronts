package combat;

import java.awt.Color;

import core.GridPosition;
import core.GridMap;
import game.GameEngine;

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
        engine.changeColor(color);
        engine.drawSolidCircle(x, y, 4);
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
