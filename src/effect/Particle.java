/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package effect;

import java.awt.Color;

import game.GameEngine;

/** Small visual particle used by burst effects. */
public class Particle {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double life;
    private final double maxLife;
    private final double size;
    private final Color color;

    public Particle(double x, double y, double life, Color color) {
        this(x, y, 0.0, -20.0, life, 3.0, color);
    }

    public Particle(double x, double y, double vx, double vy, double life, double size, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.maxLife = life;
        this.size = size;
        this.color = color;
    }

    public void update(double dt) {
        x += vx * dt;
        y += vy * dt;
        vy += 90 * dt;
        vx *= 0.96;
        vy *= 0.96;
        life -= dt;
    }

    public boolean isFinished() {
        return life <= 0.0;
    }

    public void draw(GameEngine engine) {
        float alpha = (float) Math.max(0.0, Math.min(1.0, life / maxLife));
        engine.setAlpha(alpha);
        engine.changeColor(color);
        engine.drawSolidCircle(x, y, size * alpha);
        engine.setAlpha(1.0f);
    }
}
