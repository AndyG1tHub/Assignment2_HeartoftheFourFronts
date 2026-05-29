package enemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridPosition;
import core.GridMap;
import building.Building;
import enemy.enemies.BossEnemy;
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
    private int currentLevel = 1;
    private boolean finalEliteWaveSpawned = false;

    public EnemySpawner(GridMap map, EnemyFactory enemyFactory, DifficultyManager difficultyManager) {
        this.map = map;
        this.enemyFactory = enemyFactory;
        this.difficultyManager = difficultyManager;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
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

        // Level 1-2: Spawn final elite wave instead of boss
        if (shouldSpawnFinalEliteWave(waveManager)) {
            int eliteCount = (currentLevel == 1) ? 3 : 4;
            for (int i = 0; i < eliteCount; i++) {
                Enemy elite = enemyFactory.createEnemy(EnemyType.ELITE, getSpawnPosition());
                elite.snapToMap(map);
                enemies.add(elite);
            }
            finalEliteWaveSpawned = true;
            waveManager.markBossSpawned(); // Mark as "boss spawned" to stop regular spawning
        }

        // Level 3-5: Spawn boss
        if (shouldSpawnBoss(waveManager)) {
            int bossHp = (int)(GameConfig.getBossHp(currentLevel) * difficultyManager.getEnemyHpMultiplier());
            Enemy boss = enemyFactory.createEnemy(EnemyType.BOSS, getSpawnPosition());
            boss.snapToMap(map);
            enemies.add(boss);
            waveManager.markBossSpawned();
        }

        // Stop spawning only after boss/final wave is defeated
        if (!waveManager.hasWon() && shouldSpawn(waveManager)) {
            spawnEnemy(waveManager);
            spawnTimer = 0.0;
        }
    }

    private boolean shouldSpawnFinalEliteWave(WaveManager waveManager) {
        // Level 1-2: spawn final elite wave instead of boss
        return !finalEliteWaveSpawned
                && !waveManager.hasBossSpawned()
                && waveManager.getStage() >= GameConfig.TOTAL_STAGES
                && currentLevel <= 2;
    }

    private boolean shouldSpawnBoss(WaveManager waveManager) {
        // Level 3-5: spawn boss
        return !waveManager.hasBossSpawned()
                && waveManager.getStage() >= GameConfig.TOTAL_STAGES
                && currentLevel >= 3;
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

        // Base enemy composition depends on current LEVEL
        double tankChance = 0.0;
        double assassinChance = 0.0;
        double archerChance = 0.0;

        switch (currentLevel) {
            case 1:
                tankChance = GameConfig.LEVEL_ONE_TANK_CHANCE;
                assassinChance = GameConfig.LEVEL_ONE_ASSASSIN_CHANCE;
                archerChance = GameConfig.LEVEL_ONE_ARCHER_CHANCE;
                break;
            case 2:
                tankChance = GameConfig.LEVEL_TWO_TANK_CHANCE;
                assassinChance = GameConfig.LEVEL_TWO_ASSASSIN_CHANCE;
                archerChance = GameConfig.LEVEL_TWO_ARCHER_CHANCE;
                break;
            case 3:
                tankChance = GameConfig.LEVEL_THREE_TANK_CHANCE;
                assassinChance = GameConfig.LEVEL_THREE_ASSASSIN_CHANCE;
                archerChance = GameConfig.LEVEL_THREE_ARCHER_CHANCE;
                break;
            case 4:
                tankChance = GameConfig.LEVEL_FOUR_TANK_CHANCE;
                assassinChance = GameConfig.LEVEL_FOUR_ASSASSIN_CHANCE;
                archerChance = GameConfig.LEVEL_FOUR_ARCHER_CHANCE;
                break;
            case 5:
            default:
                tankChance = GameConfig.LEVEL_FIVE_TANK_CHANCE;
                assassinChance = GameConfig.LEVEL_FIVE_ASSASSIN_CHANCE;
                archerChance = GameConfig.LEVEL_FIVE_ARCHER_CHANCE;
                break;
        }

        // Stage increases special enemy chances (more variety as stage progresses)
        double stageMultiplier = 1.0 + (stage - 1) * 0.15; // +15% per stage
        tankChance *= stageMultiplier;
        assassinChance *= stageMultiplier;
        archerChance *= stageMultiplier;

        // Calculate cumulative probabilities
        double tankLimit = tankChance;
        double assassinLimit = tankLimit + assassinChance;
        double archerLimit = assassinLimit + archerChance;

        if (roll < tankLimit) return EnemyType.TANK;
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

    public void updateEnemies(double dt, EnemyAI enemyAI, EconomyManager economy, ScoreManager score,
            WaveManager waveManager, List<Building> buildings) {
        for (Enemy enemy : new ArrayList<>(enemies)) {
            if (enemy instanceof BossEnemy) {
                ((BossEnemy) enemy).setBuildings(buildings);
            }
        }
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, enemyAI);
            if (enemy.isDead()) {
                if (enemy.getType() == EnemyType.BOSS) {
                    waveManager.markBossDefeated();
                }
                economy.addMoney(enemy.getMoneyReward());
                score.addKillScore(enemy.getScoreReward());
                iterator.remove();
                SoundManager sm = SoundManager.getInstance();
                if (sm != null) sm.playEnemyDeath();
            }
        }

        // Level 1-2: Check if final elite wave is defeated
        if (currentLevel <= 2 && finalEliteWaveSpawned && !waveManager.hasWon()) {
            boolean allElitesDefeated = true;
            for (Enemy enemy : enemies) {
                if (enemy.getType() == EnemyType.ELITE) {
                    allElitesDefeated = false;
                    break;
                }
            }
            if (allElitesDefeated) {
                waveManager.markFinalWaveDefeated();
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
