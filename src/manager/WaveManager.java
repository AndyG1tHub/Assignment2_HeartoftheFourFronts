package manager;

import game.GameConfig;

/** Tracks elapsed time and exposes the current pressure stage. */
public class WaveManager {
    private double elapsedTime;
    private int stage = 1;
    private double lastEliteTime = 0;
    private double prepTimer = 5.0;
    private boolean bossSpawned;
    private boolean bossDefeated;
    private boolean finalEliteWave;

    /** Advances wave time and handles stage transitions. */
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

    /** Returns true when an elite wave should spawn. */
    public boolean isEliteWave() {
        // Increase elite spawn interval from 40s to 60s
        return !isPrepTime() && !bossSpawned && elapsedTime - lastEliteTime >= 60.0;
    }

    public void markEliteSpawned() {
        lastEliteTime = elapsedTime;
    }

    public int getEliteCount() {
        // Reduce elite count: max 2 instead of 4
        return Math.min(stage, 2);
    }

    public boolean isBossActive() {
        return bossSpawned && !bossDefeated && !finalEliteWave;
    }

    public boolean isFinalEliteWaveActive() {
        return bossSpawned && !bossDefeated && finalEliteWave;
    }

    public boolean isFinalEliteWave() {
        return finalEliteWave;
    }

    public void markBossDefeated() {
        bossDefeated = true;
    }

    public void markBossSpawned() {
        bossSpawned = true;
        finalEliteWave = false;
    }

    public void markFinalEliteWaveSpawned() {
        bossSpawned = true;
        finalEliteWave = true;
    }

    public boolean hasBossSpawned() {
        return bossSpawned;
    }

    public void restoreBossState(boolean bossSpawned, boolean bossDefeated, boolean finalEliteWave) {
        this.bossSpawned = bossSpawned;
        this.bossDefeated = bossDefeated;
        this.finalEliteWave = finalEliteWave;
    }

    /** Returns the spawn speed multiplier for the current stage. */
    public double getSpawnIntervalMultiplier() {
        // Boss phase: spawn enemies much faster!
        if (bossSpawned && !bossDefeated) {
            return 0.25;  // 75% faster spawn during boss fight
        }
        return Math.max(0.35, 1.0 - (stage - 1) * 0.12);
    }

    /** Returns true once the final wave or boss is defeated. */
    public boolean hasWon() {
        // Win condition: Boss/Final wave defeated (marked by bossDefeated)
        return bossDefeated;
    }

    public boolean canCheckWin() {
        // Only check win after boss/final wave has spawned
        return bossSpawned;
    }

    public void markFinalWaveDefeated() {
        // For Level 1-2, mark final elite wave as defeated
        bossDefeated = true;
    }

    public int getStage() {
        return stage;
    }

    public int getWaveNumber() {
        return (int) (elapsedTime / GameConfig.WAVE_LENGTH_SECONDS) + 1;
    }

    /** Restores wave timing from saved data. */
    public void setElapsedTime(double elapsedTime, int stage) {
        this.elapsedTime = elapsedTime;
        this.stage = stage;
        this.lastEliteTime = elapsedTime - GameConfig.BOSS_INTERVAL;
        this.prepTimer = 0;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }
}
