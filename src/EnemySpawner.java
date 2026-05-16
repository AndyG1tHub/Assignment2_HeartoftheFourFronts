import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    }

    private Direction chooseSpawnDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    private EnemyType chooseEnemyType(WaveManager waveManager) {
        int stage = waveManager.getStage();
        if (stage >= 3 && random.nextDouble() < 0.08) {
            return EnemyType.BOSS;
        }
        if (stage >= 2 && random.nextDouble() < 0.25) {
            return EnemyType.RAVAGER;
        }
        return EnemyType.VOIDLING;
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
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(dt, enemyAI);
            if (enemy.isDead()) {
                economy.addMoney(enemy.getMoneyReward());
                score.addKillScore(enemy.getScoreReward());
                iterator.remove();
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
