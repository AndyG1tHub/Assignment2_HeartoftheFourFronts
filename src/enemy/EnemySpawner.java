package enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import enemy.enemies.HealerEnemy;
import util.Direction;
import game.GameEngine;
import game.GameConfig;
import manager.DifficultyManager;
import manager.WaveManager;
import manager.EconomyManager;
import manager.ScoreManager;
import manager.SoundManager;

/** Spawns enemies from four map edges and owns the active enemy list. */
public class EnemySpawner {
    private final GridMap map;
    private final EnemyFactory enemyFactory;
    private final DifficultyManager difficultyManager;
    private final Random random = new Random();
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private double spawnTimer;

    public EnemySpawner(GridMap map, EnemyFactory enemyFactory, DifficultyManager difficultyManager) {
        this.map = map;
        this.enemyFactory = enemyFactory;
        this.difficultyManager = difficultyManager;
    }

    public void update(double dt, WaveManager waveManager) {
        spawnTimer += dt;
        if (shouldSpawn(waveManager)) {
            spawnEnemy(waveManager);
            spawnTimer = 0.0;
        }
    }

    private boolean shouldSpawn(WaveManager waveManager) {
        double interval = GameConfig.DEFAULT_SPAWN_INTERVAL
                * difficultyManager.getSpawnIntervalMultiplier()
                * waveManager.getSpawnIntervalMultiplier();
        return spawnTimer >= interval;
    }

    private void spawnEnemy(WaveManager waveManager) {
        Direction direction = chooseSpawnDirection();
        Enemy enemy = enemyFactory.createEnemy(chooseEnemyType(waveManager), getSpawnPosition(direction));
        enemy.snapToMap(map);
        enemies.add(enemy);
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playEnemySpawn();
    }

    private Direction chooseSpawnDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    private EnemyType chooseEnemyType(WaveManager waveManager) {
        int stage = waveManager.getStage();
        double roll = random.nextDouble();
        if (stage >= 3) {
            if (roll < 0.18) {
                return EnemyType.TANK;
            }
            if (roll < 0.34) {
                return EnemyType.ASSASSIN;
            }
            if (roll < 0.50) {
                return EnemyType.ARCHER;
            }
            if (roll < 0.62) {
                return EnemyType.HEALER;
            }
            return EnemyType.MELEE;
        }
        if (stage >= 2) {
            if (roll < 0.20) {
                return EnemyType.TANK;
            }
            if (roll < 0.35) {
                return EnemyType.ASSASSIN;
            }
            if (roll < 0.50) {
                return EnemyType.ARCHER;
            }
            if (roll < 0.58) {
                return EnemyType.HEALER;
            }
            return EnemyType.MELEE;
        }
        if (roll < 0.20) {
            return EnemyType.ASSASSIN;
        }
        if (roll < 0.35) {
            return EnemyType.ARCHER;
        }
        return EnemyType.MELEE;
    }

    private GridPosition getSpawnPosition(Direction direction) {
        int middleRow = GameConfig.GRID_ROWS / 2;
        int middleCol = GameConfig.GRID_COLS / 2;
        if (direction == Direction.NORTH) {
            return new GridPosition(0, middleCol);
        }
        if (direction == Direction.SOUTH) {
            return new GridPosition(GameConfig.GRID_ROWS - 1, middleCol);
        }
        if (direction == Direction.WEST) {
            return new GridPosition(middleRow, 0);
        }
        return new GridPosition(middleRow, GameConfig.GRID_COLS - 1);
    }

    public void updateEnemies(double dt, EnemyAI enemyAI, EconomyManager economy, ScoreManager score) {
        for (Enemy enemy : new ArrayList<Enemy>(enemies)) {
            if (enemy instanceof HealerEnemy) {
                ((HealerEnemy) enemy).supportAllies(dt, enemies);
            }
        }
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, enemyAI);
            if (enemy.isDead()) {
                economy.addMoney(enemy.getMoneyReward());
                score.addKillScore(enemy.getScoreReward());
                iterator.remove();
                SoundManager sm = SoundManager.getInstance();
                if (sm != null) sm.playEnemyDeath();
            }
        }
    }

    public void draw(GameEngine engine, GridMap map) {
        for (Enemy enemy : enemies) {
            enemy.draw(engine, map);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
