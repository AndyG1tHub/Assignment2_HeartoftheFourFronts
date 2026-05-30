/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)


 * Starts and updates disaster events from stage 2 onward.
 * At difficulty-scaled intervals, creates a MeteorStrike.
 * Targets a random building (50%) or a random empty tile.
 * Updates and draws active disasters each frame; removes finished ones.
 */
package event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import building.Building;
import combat.MeteorStrike;
import game.GameEngine;
import manager.WaveManager;

/** Starts and updates disaster events in later waves. */
public class DisasterManager {
    private final List<Disaster> disasters = new ArrayList<Disaster>();
    private final Random random = new Random();
    private double timer;
    private double disasterInterval;

    public void setDisasterInterval(double interval) {
        this.disasterInterval = interval;
    }

    public void update(double dt, WaveManager waves, GridMap map,
            List<Enemy> enemies, List<Building> buildings) {
        timer += dt;
        maybeStartDisaster(waves, map, buildings);
        updateActiveDisasters(dt, enemies, buildings);
    }

    private void maybeStartDisaster(WaveManager waves, GridMap map, List<Building> buildings) {
        if (waves.getStage() < 2 || timer < disasterInterval) {
            return;
        }
        disasters.add(createRandomDisaster(map, buildings));
        timer = 0.0;
    }

    private Disaster createRandomDisaster(GridMap map, List<Building> buildings) {
        GridPosition position;

        // 50% chance to target a building
        if (random.nextDouble() < 0.5 && !buildings.isEmpty()) {
            Building targetBuilding = buildings.get(random.nextInt(buildings.size()));
            position = targetBuilding.getPosition();
        } else {
            // Random empty position
            List<GridPosition> empty = map.getAllEmptyPositions();
            position = empty.isEmpty() ? map.getBasePosition() : empty.get(random.nextInt(empty.size()));
        }

        return new MeteorStrike(position);
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
