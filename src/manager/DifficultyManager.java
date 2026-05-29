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
            return 0.7;  // 30% less HP
        }
        if (difficulty == Difficulty.HARD) {
            return 1.2;  // 20% more HP
        }
        return 1.1;  // Normal: 10% more HP
    }

    public double getSpawnIntervalMultiplier() {
        if (difficulty == Difficulty.EASY) {
            return 1.4;  // 40% slower spawn
        }
        if (difficulty == Difficulty.HARD) {
            return 0.8;  // 20% faster spawn
        }
        return 0.9;  // Normal: 10% faster spawn
    }

    public double getDisasterInterval() {
        double base = 10.0;
        if (difficulty == Difficulty.EASY) return base * 1.8;  // Disasters every 18s
        if (difficulty == Difficulty.HARD) return base * 0.8;  // Disasters every 8s
        return base * 0.9;  // Normal: every 9s
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
