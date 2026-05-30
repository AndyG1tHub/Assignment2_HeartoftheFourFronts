/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import effect.ParticleSystem;
import effect.RewardPoint;
import game.GameEngine;
import game.GameConfig;

/** Spawns, updates, draws and collects reward points. */
public class RewardPointManager {
    private final List<RewardPoint> rewardPoints = new ArrayList<RewardPoint>();
    private final Random random = new Random();
    private double spawnTimer;

    public void update(double dt, GridMap map) {
        spawnTimer += dt;
        if (spawnTimer >= GameConfig.REWARD_SPAWN_INTERVAL) {
            // 75% chance to spawn
            if (random.nextDouble() < GameConfig.REWARD_SPAWN_CHANCE) {
                spawnRewardPoint(map);
            }
            spawnTimer = 0.0;
        }
        removeFinished(dt);
    }

    private void spawnRewardPoint(GridMap map) {
        java.util.List<GridPosition> empty = map.getAllEmptyPositions();
        if (!empty.isEmpty()) {
            rewardPoints.add(new RewardPoint(empty.get(random.nextInt(empty.size()))));
            SoundManager sm = SoundManager.getInstance();
            if (sm != null) sm.playRewardSpawn();
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

    public boolean handleClick(GridPosition position, EconomyManager economy, ScoreManager score,
            GridMap map, ParticleSystem particleSystem) {
        for (RewardPoint point : rewardPoints) {
            if (point.isClicked(position)) {
                point.collect(economy, score);
                if (particleSystem != null) {
                    particleSystem.spawnRewardText(map, position,
                            GameConfig.REWARD_MONEY, GameConfig.REWARD_SCORE);
                }
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
