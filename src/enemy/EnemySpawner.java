package enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import enemy.enemies.HealerEnemy;
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
        Enemy enemy = enemyFactory.createEnemy(chooseEnemyType(waveManager), getSpawnPosition());
        enemy.snapToMap(map);
        enemies.add(enemy);
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playEnemySpawn();
    }

    private EnemyType chooseEnemyType(WaveManager waveManager) {
        int stage = waveManager.getStage();
        double roll = random.nextDouble();
        if (stage >= 3) {
            double tankLimit = GameConfig.STAGE_THREE_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_THREE_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_THREE_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_THREE_HEALER_CHANCE;
            if (roll < tankLimit) {
                return EnemyType.TANK;
            }
            if (roll < assassinLimit) {
                return EnemyType.ASSASSIN;
            }
            if (roll < archerLimit) {
                return EnemyType.ARCHER;
            }
            if (roll < healerLimit) {
                return EnemyType.HEALER;
            }
            return EnemyType.MELEE;
        }
        if (stage >= 2) {
            double tankLimit = GameConfig.STAGE_TWO_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_TWO_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_TWO_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_TWO_HEALER_CHANCE;
            if (roll < tankLimit) {
                return EnemyType.TANK;
            }
            if (roll < assassinLimit) {
                return EnemyType.ASSASSIN;
            }
            if (roll < archerLimit) {
                return EnemyType.ARCHER;
            }
            if (roll < healerLimit) {
                return EnemyType.HEALER;
            }
            return EnemyType.MELEE;
        }
        double assassinLimit = GameConfig.STAGE_ONE_ASSASSIN_CHANCE;
        double archerLimit = assassinLimit + GameConfig.STAGE_ONE_ARCHER_CHANCE;
        if (roll < assassinLimit) {
            return EnemyType.ASSASSIN;
        }
        if (roll < archerLimit) {
            return EnemyType.ARCHER;
        }
        return EnemyType.MELEE;
    }

    private GridPosition getSpawnPosition() {
        int side = random.nextInt(4);
        if (side == 0) {
            return new GridPosition(0, random.nextInt(GameConfig.GRID_COLS));
        }
        if (side == 1) {
            return new GridPosition(GameConfig.GRID_ROWS - 1, random.nextInt(GameConfig.GRID_COLS));
        }
        if (side == 2) {
            return new GridPosition(random.nextInt(GameConfig.GRID_ROWS), 0);
        }
        return new GridPosition(random.nextInt(GameConfig.GRID_ROWS), GameConfig.GRID_COLS - 1);
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
