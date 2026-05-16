import java.awt.Color;

/** Small visual particle used by future effects. */
public class Particle {
    private double x;
    private double y;
    private double life;
    private final Color color;

    public Particle(double x, double y, double life, Color color) {
        this.x = x;
        this.y = y;
        this.life = life;
        this.color = color;
    }

    public void update(double dt) {
        y -= 20 * dt;
        life -= dt;
    }

    public boolean isFinished() {
        return life <= 0.0;
    }

    public void draw(GameEngine engine) {
        engine.changeColor(color);
        engine.drawSolidCircle(x, y, 3);
    }
}
