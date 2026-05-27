package manager;

import game.GameConfig;

/** Tracks elapsed time and exposes the current pressure stage. */
public class WaveManager {
    private double elapsedTime;
    private int stage = 1;
    private double lastBossTime = 0;
    private double prepTimer = 5.0;

    public void update(double dt, ScoreManager score) {
        int oldStage = stage;
        if (prepTimer > 0) {
            prepTimer = Math.max(0, prepTimer - dt);
            return;
        }
        elapsedTime += dt;
        stage = calculateStage();
        if (stage > oldStage) {
            score.addWaveScore(stage * 250);
            prepTimer = 5.0;
            SoundManager sm = SoundManager.getInstance();
            if (sm != null) sm.playWaveStart();
        }
    }

    private int calculateStage() {
        double t = elapsedTime;
        for (int i = GameConfig.TOTAL_STAGES; i >= 1; i--) {
            if (t >= GameConfig.WAVE_LENGTH_SECONDS * (i - 1)) {
                return i;
            }
        }
        return 1;
    }

    public boolean isPrepTime() {
        return prepTimer > 0;
    }

    public double getPrepTimer() {
        return prepTimer;
    }

    public boolean isBossWave() {
        return !isPrepTime() && elapsedTime - lastBossTime >= GameConfig.BOSS_INTERVAL;
    }

    public void markBossSpawned() {
        lastBossTime = elapsedTime;
    }

    public int getBossCount() {
        return Math.min(stage, 4);
    }

    public double getSpawnIntervalMultiplier() {
        return Math.max(0.35, 1.0 - (stage - 1) * 0.12);
    }

    public boolean hasWon() {
        return elapsedTime >= GameConfig.WAVE_LENGTH_SECONDS * GameConfig.TOTAL_STAGES;
    }

    public int getStage() {
        return stage;
    }

    public int getWaveNumber() {
        return (int) (elapsedTime / GameConfig.WAVE_LENGTH_SECONDS) + 1;
    }

    public void setElapsedTime(double elapsedTime, int stage) {
        this.elapsedTime = elapsedTime;
        this.stage = stage;
        this.lastBossTime = elapsedTime - GameConfig.BOSS_INTERVAL;
        this.prepTimer = 0;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }
}
