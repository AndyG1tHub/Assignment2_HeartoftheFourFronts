import java.awt.Color;
import java.util.List;

/** Warning marker followed by one burst of damage. */
public class MeteorStrike extends Disaster {
    private double warningTimer;
    private boolean struck;

    public MeteorStrike(GridPosition position) {
        super(EventType.METEOR_STRIKE, position, 1, 45, 2.5);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        warningTimer += dt;
    }

    @Override
    public void applyEffect(List<Enemy> enemies, List<Building> buildings) {
        if (struck || warningTimer < 1.2) {
            return;
        }
        for (Enemy enemy : enemies) {
            if (affects(enemy.getGridPosition())) {
                enemy.takeDamage(damage);
            }
        }
        for (Building building : buildings) {
            if (affects(building.getPosition())) {
                building.takeDamage(damage);
            }
        }
        struck = true;
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playMeteorDisaster();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        Color color = struck ? new Color(255, 130, 45) : new Color(255, 230, 90);
        drawArea(engine, map, color);
    }
}
