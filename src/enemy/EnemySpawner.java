package enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import enemy.enemies.FinalBossEnemy;
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
    private final List<Enemy> enemies = new ArrayList<>();
    private double spawnTimer;

    public EnemySpawner(GridMap map, EnemyFactory enemyFactory, DifficultyManager difficultyManager) {
        this.map = map;
        this.enemyFactory = enemyFactory;
        this.difficultyManager = difficultyManager;
    }

    public void update(double dt, WaveManager waveManager) {
        if (waveManager.isPrepTime()) return;
        spawnTimer += dt;

        if (waveManager.isEliteWave()) {
            for (int i = 0; i < waveManager.getEliteCount(); i++) {
                Enemy elite = enemyFactory.createEnemy(EnemyType.ELITE, getSpawnPosition());
                elite.snapToMap(map);
                enemies.add(elite);
            }
            waveManager.markEliteSpawned();
        }

        if (waveManager.isFinalBossTime()) {
            Enemy boss = enemyFactory.createEnemy(EnemyType.FINAL_BOSS, getSpawnPosition());
            boss.snapToMap(map);
            enemies.add(boss);
            waveManager.markFinalBossSpawned();
        }

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
        if (stage >= 5) {
            double tankLimit = GameConfig.STAGE_FIVE_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_FIVE_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_FIVE_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_FIVE_HEALER_CHANCE;
            if (roll < tankLimit) return EnemyType.TANK;
            if (roll < assassinLimit) return EnemyType.ASSASSIN;
            if (roll < archerLimit) return EnemyType.ARCHER;
            if (roll < healerLimit) return EnemyType.HEALER;
            return EnemyType.MELEE;
        }
        if (stage >= 4) {
            double tankLimit = GameConfig.STAGE_FOUR_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_FOUR_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_FOUR_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_FOUR_HEALER_CHANCE;
            if (roll < tankLimit) return EnemyType.TANK;
            if (roll < assassinLimit) return EnemyType.ASSASSIN;
            if (roll < archerLimit) return EnemyType.ARCHER;
            if (roll < healerLimit) return EnemyType.HEALER;
            return EnemyType.MELEE;
        }
        if (stage >= 3) {
            double tankLimit = GameConfig.STAGE_THREE_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_THREE_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_THREE_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_THREE_HEALER_CHANCE;
            if (roll < tankLimit) return EnemyType.TANK;
            if (roll < assassinLimit) return EnemyType.ASSASSIN;
            if (roll < archerLimit) return EnemyType.ARCHER;
            if (roll < healerLimit) return EnemyType.HEALER;
            return EnemyType.MELEE;
        }
        if (stage >= 2) {
            double tankLimit = GameConfig.STAGE_TWO_TANK_CHANCE;
            double assassinLimit = tankLimit + GameConfig.STAGE_TWO_ASSASSIN_CHANCE;
            double archerLimit = assassinLimit + GameConfig.STAGE_TWO_ARCHER_CHANCE;
            double healerLimit = archerLimit + GameConfig.STAGE_TWO_HEALER_CHANCE;
            if (roll < tankLimit) return EnemyType.TANK;
            if (roll < assassinLimit) return EnemyType.ASSASSIN;
            if (roll < archerLimit) return EnemyType.ARCHER;
            if (roll < healerLimit) return EnemyType.HEALER;
            return EnemyType.MELEE;
        }
        double assassinLimit = GameConfig.STAGE_ONE_ASSASSIN_CHANCE;
        double archerLimit = assassinLimit + GameConfig.STAGE_ONE_ARCHER_CHANCE;
        if (roll < assassinLimit) return EnemyType.ASSASSIN;
        if (roll < archerLimit) return EnemyType.ARCHER;
        return EnemyType.MELEE;
    }

    private GridPosition getSpawnPosition() {
        int maxAttempts = 50;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int side = random.nextInt(4);
            GridPosition position;
            if (side == 0) {
                position = new GridPosition(0, random.nextInt(GameConfig.GRID_COLS));
            } else if (side == 1) {
                position = new GridPosition(GameConfig.GRID_ROWS - 1, random.nextInt(GameConfig.GRID_COLS));
            } else if (side == 2) {
                position = new GridPosition(random.nextInt(GameConfig.GRID_ROWS), 0);
            } else {
                position = new GridPosition(random.nextInt(GameConfig.GRID_ROWS), GameConfig.GRID_COLS - 1);
            }
            if (map.isWalkable(position)) {
                return position;
            }
        }
        return new GridPosition(0, 0);
    }

    public void updateEnemies(double dt, EnemyAI enemyAI, EconomyManager economy, ScoreManager score, WaveManager waveManager) {
        for (Enemy enemy : new ArrayList<>(enemies)) {
            if (enemy instanceof HealerEnemy) {
                ((HealerEnemy) enemy).supportAllies(dt, enemies);
            }
            if (enemy instanceof FinalBossEnemy) {
                ((FinalBossEnemy) enemy).applySkills(enemies);
            }
        }
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, enemyAI);
            if (enemy.isDead()) {
                if (enemy.getType() == EnemyType.FINAL_BOSS) {
                    waveManager.markFinalBossDefeated();
                }
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
