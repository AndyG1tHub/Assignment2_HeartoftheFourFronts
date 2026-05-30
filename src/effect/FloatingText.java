/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)



 * Transient floating text that rises and fades over 1.5 seconds.
 * Displays feedback like "BUILD", "UPGRADE", and "+SCORE" above tiles.
 * Rendered in bold with alpha decreasing as lifetime expires.
 */
package effect;

import java.awt.Color;

import game.GameEngine;

public class FloatingText {
    private double x;
    private double y;
    private final String text;
    private final Color color;
    private double life;
    private final double maxLife = 1.5;
    private final double riseSpeed = 30.0;

    public FloatingText(double x, double y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.life = maxLife;
    }

    public void update(double dt) {
        y -= riseSpeed * dt;
        life -= dt;
    }

    public boolean isFinished() {
        return life <= 0;
    }

    public void draw(GameEngine engine) {
        if (life <= 0) {
            return;
        }

        float alpha = (float) Math.min(1.0, life / maxLife);
        Color drawColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                (int) (alpha * 255));

        engine.changeColor(drawColor);

        int textWidth = engine.textWidth(text, "Arial", 14);
        engine.drawBoldText(x - textWidth / 2, y, text, "Arial", 14);
    }
}
