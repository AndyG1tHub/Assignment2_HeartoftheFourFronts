import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Starts and updates disaster events in later waves. */
public class DisasterManager {
    private final List<Disaster> disasters = new ArrayList<Disaster>();
    private final Random random = new Random();
    private double timer;

    public void update(double dt, WaveManager waves, GridMap map,
            List<Enemy> enemies, List<Building> buildings) {
        timer += dt;
        maybeStartDisaster(waves, map);
        updateActiveDisasters(dt, enemies, buildings);
    }

    private void maybeStartDisaster(WaveManager waves, GridMap map) {
        if (waves.getStage() < 2 || timer < 10.0) {
            return;
        }
        disasters.add(createRandomDisaster(map));
        timer = 0.0;
    }

    private Disaster createRandomDisaster(GridMap map) {
        List<GridPosition> empty = map.getAllEmptyPositions();
        GridPosition position = empty.isEmpty() ? map.getBasePosition() : empty.get(random.nextInt(empty.size()));
        return random.nextBoolean() ? new FireZone(position) : new MeteorStrike(position);
    }

    private void updateActiveDisasters(double dt, List<Enemy> enemies, List<Building> buildings) {
        Iterator<Disaster> iterator = disasters.iterator();
        while (iterator.hasNext()) {
            Disaster disaster = iterator.next();
            disaster.update(dt);
            disaster.applyEffect(enemies, buildings);
            if (disaster.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void draw(GameEngine engine, GridMap map) {
        for (Disaster disaster : disasters) {
            disaster.draw(engine, map);
        }
    }
}
