/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package manager;

import game.Difficulty;

/**
 * <p>Translates the selected {@link Difficulty} into gameplay multiplier values for enemy HP, damage, spawn rate and disaster intervals.</p>
 */
public class DifficultyManager {
    private Difficulty difficulty;
    private double adaptivePressure = 1.0;

    public DifficultyManager(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /** Returns the HP multiplier for the selected difficulty. */
    public double getEnemyHpMultiplier() {
        if (difficulty == Difficulty.EASY) {
            return 0.7;  // 30% less HP
        }
        if (difficulty == Difficulty.HARD) {
            return 1.2;  // 20% more HP
        }
        return 1.1;  // Normal: 10% more HP
    }

    /** Returns the spawn interval multiplier for the selected difficulty. */
    public double getSpawnIntervalMultiplier() {
        if (difficulty == Difficulty.EASY) {
            return 1.4;  // 40% slower spawn
        }
        if (difficulty == Difficulty.HARD) {
            return 0.8;  // 20% faster spawn
        }
        return 0.9;  // Normal: 10% faster spawn
    }

    /** Adjusts pressure based on current player performance. */
    public void updateAdaptivePressure(double baseHpRatio, int money, int kills, double elapsedTime) {
        double target = 1.0;
        if (baseHpRatio > 0.75) {
            target += 0.12;
        } else if (baseHpRatio < 0.35) {
            target -= 0.18;
        }
        if (money >= 220) {
            target += 0.08;
        } else if (money < 70) {
            target -= 0.08;
        }
        double expectedKills = Math.max(1.0, elapsedTime / 8.0);
        if (kills > expectedKills * 1.25) {
            target += 0.10;
        } else if (kills < expectedKills * 0.65 && elapsedTime > 30.0) {
            target -= 0.08;
        }
        adaptivePressure += (clamp(target, 0.75, 1.25) - adaptivePressure) * 0.05;
    }

    public double getAdaptiveSpawnMultiplier() {
        return 1.0 / adaptivePressure;
    }

    public double getAdaptiveSpecialEnemyMultiplier() {
        return adaptivePressure;
    }

    public double getAdaptivePressure() {
        return adaptivePressure;
    }

    /** Returns how often disasters should trigger. */
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

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
