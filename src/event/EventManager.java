package event;

import core.GridPosition;
import core.GridMap;
import building.Building;
import enemy.EnemySpawner;
import game.GameEngine;
import manager.RewardPointManager;
import manager.WaveManager;
import manager.EconomyManager;
import manager.ScoreManager;

/** Facade that coordinates reward points and disasters. */
public class EventManager {
    private final RewardPointManager rewardPointManager;
    private final DisasterManager disasterManager;

    public EventManager() {
        rewardPointManager = new RewardPointManager();
        disasterManager = new DisasterManager();
    }

    public void update(double dt, GridMap map, WaveManager waves,
            EnemySpawner spawner, java.util.List<Building> buildings) {
        rewardPointManager.update(dt, map);
        disasterManager.update(dt, waves, map, spawner.getEnemies(), buildings);
    }

    public boolean handleClick(GridPosition position, EconomyManager economy, ScoreManager score) {
        return rewardPointManager.handleClick(position, economy, score);
    }

    public void draw(GameEngine engine, GridMap map) {
        rewardPointManager.draw(engine, map);
        disasterManager.draw(engine, map);
    }
}
