package manager;

import game.Difficulty;

/** Converts Difficulty into gameplay multipliers. */
public class DifficultyManager {
    private Difficulty difficulty;

    public DifficultyManager(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public double getEnemyHpMultiplier() {
        if (difficulty == Difficulty.EASY) {
            return 0.8;
        }
        if (difficulty == Difficulty.HARD) {
            return 1.35;
        }
        return 1.0;
    }

    public double getSpawnIntervalMultiplier() {
        if (difficulty == Difficulty.EASY) {
            return 1.25;
        }
        if (difficulty == Difficulty.HARD) {
            return 0.75;
        }
        return 1.0;
    }

    public double getDisasterInterval() {
        double base = 10.0;
        if (difficulty == Difficulty.EASY) return base * 1.5;
        if (difficulty == Difficulty.HARD) return base * 0.7;
        return base;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
