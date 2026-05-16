import java.awt.Color;
import java.util.List;

/** Persistent fire area that slowly damages units and buildings. */
public class FireZone extends Disaster {
    private double tickTimer;

    public FireZone(GridPosition position) {
        super(EventType.FIRE_ZONE, position, 1, 5, 5.0);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        tickTimer += dt;
    }

    @Override
    public void applyEffect(List<Enemy> enemies, List<Building> buildings) {
        if (tickTimer < 1.0) {
            return;
        }
        damageEnemies(enemies);
        damageBuildings(buildings);
        tickTimer = 0.0;
    }

    private void damageEnemies(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (affects(enemy.getGridPosition())) {
                enemy.takeDamage(damage);
            }
        }
    }

    private void damageBuildings(List<Building> buildings) {
        for (Building building : buildings) {
            if (affects(building.getPosition())) {
                building.takeDamage(damage);
            }
        }
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        drawArea(engine, map, new Color(230, 90, 35));
    }
}
