package manager;

import game.GameConfig;

/** Tracks elapsed time and exposes the current pressure stage. */
public class WaveManager {
    private double elapsedTime;
    private int stage = 1;
    private double lastBossTime = -GameConfig.BOSS_INTERVAL;

    public void update(double dt, ScoreManager score) {
        int oldStage = stage;
        elapsedTime += dt;
        stage = calculateStage();
        if (stage > oldStage) {
            score.addWaveScore(stage * 250);
            SoundManager sm = SoundManager.getInstance();
            if (sm != null) sm.playWaveStart();
        }
    }

    private int calculateStage() {
        if (elapsedTime >= GameConfig.WAVE_LENGTH_SECONDS * 2) {
            return 3;
        }
        if (elapsedTime >= GameConfig.WAVE_LENGTH_SECONDS) {
            return 2;
        }
        return 1;
    }

    public boolean isBossWave() {
        return elapsedTime - lastBossTime >= GameConfig.BOSS_INTERVAL;
    }

    public void markBossSpawned() {
        lastBossTime = elapsedTime;
    }

    public int getBossCount() {
        int bosses = (int) (elapsedTime / GameConfig.BOSS_INTERVAL);
        return Math.min(bosses + 1, stage);
    }

    public double getSpawnIntervalMultiplier() {
        return Math.max(0.55, 1.0 - (stage - 1) * 0.2);
    }

    public boolean hasWon() {
        return elapsedTime >= GameConfig.WAVE_LENGTH_SECONDS * 3;
    }

    public int getStage() {
        return stage;
    }

    public void setElapsedTime(double elapsedTime, int stage) {
        this.elapsedTime = elapsedTime;
        this.stage = stage;
        this.lastBossTime = elapsedTime - GameConfig.BOSS_INTERVAL;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }
}
