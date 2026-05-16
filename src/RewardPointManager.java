import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Spawns, updates, draws and collects reward points. */
public class RewardPointManager {
    private final List<RewardPoint> rewardPoints = new ArrayList<RewardPoint>();
    private final Random random = new Random();
    private double spawnTimer;

    public void update(double dt, GridMap map) {
        spawnTimer += dt;
        if (spawnTimer >= GameConfig.REWARD_SPAWN_INTERVAL) {
            spawnRewardPoint(map);
            spawnTimer = 0.0;
        }
        removeFinished(dt);
    }

    private void spawnRewardPoint(GridMap map) {
        java.util.List<GridPosition> empty = map.getAllEmptyPositions();
        if (!empty.isEmpty()) {
            rewardPoints.add(new RewardPoint(empty.get(random.nextInt(empty.size()))));
        }
    }

    private void removeFinished(double dt) {
        Iterator<RewardPoint> iterator = rewardPoints.iterator();
        while (iterator.hasNext()) {
            RewardPoint point = iterator.next();
            point.update(dt);
            if (point.isFinished()) {
                iterator.remove();
            }
        }
    }

    public boolean handleClick(GridPosition position, EconomyManager economy, ScoreManager score) {
        for (RewardPoint point : rewardPoints) {
            if (point.isClicked(position)) {
                point.collect(economy, score);
                return true;
            }
        }
        return false;
    }

    public void draw(GameEngine engine, GridMap map) {
        for (RewardPoint point : rewardPoints) {
            point.draw(engine, map);
        }
    }
}
