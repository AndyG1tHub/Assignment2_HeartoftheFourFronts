/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package event;

import core.GridPosition;
import core.GridMap;
import building.Building;
import enemy.EnemySpawner;
import game.GameEngine;
import manager.RewardPointManager;
import effect.ParticleSystem;
import manager.WaveManager;
import manager.EconomyManager;
import manager.ScoreManager;

/** Facade that coordinates reward points and disasters. */
public class EventManager {
    private final RewardPointManager rewardPointManager;
    private final DisasterManager disasterManager;

    public EventManager(double disasterInterval) {
        rewardPointManager = new RewardPointManager();
        disasterManager = new DisasterManager();
        disasterManager.setDisasterInterval(disasterInterval);
    }

    public void update(double dt, GridMap map, WaveManager waves,
            EnemySpawner spawner, java.util.List<Building> buildings) {
        rewardPointManager.update(dt, map);
        disasterManager.update(dt, waves, map, spawner.getEnemies(), buildings);
    }

    public boolean handleClick(GridPosition position, EconomyManager economy, ScoreManager score,
            GridMap map, ParticleSystem particleSystem) {
        return rewardPointManager.handleClick(position, economy, score, map, particleSystem);
    }

    public void draw(GameEngine engine, GridMap map) {
        rewardPointManager.draw(engine, map);
        disasterManager.draw(engine, map);
    }
}
