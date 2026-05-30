/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)


 * Owns the player's score and related statistics counters.
 * Tracks enemies killed, reward points collected, and buildings built.
 * Adds kill score, wave bonuses, and reward score; applies HP-loss penalty.
 * Score is clamped to never go below zero.
 * setScore() bulk-setter is used during save/load restoration.
 */
package manager;

/** Owns score counters and scoring rules. */
public class ScoreManager {
    private int score;
    private int enemiesKilled;
    private int rewardPointsCollected;
    private int buildingsBuilt;

    public void addKillScore(int amount) {
        enemiesKilled++;
        score += amount;
    }

    public void addRewardScore(int amount) {
        rewardPointsCollected++;
        score += amount;
    }

    public void addWaveScore(int amount) {
        score += amount;
    }

    public void addBuildingBuilt() {
        buildingsBuilt++;
    }

    public void applyPenalty(int amount) {
        score = Math.max(0, score - amount);
    }

    public int getScore() {
        return score;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getRewardPointsCollected() {
        return rewardPointsCollected;
    }

    public void setScore(int score, int kills, int rewards, int buildingsBuilt) {
        this.score = score;
        this.enemiesKilled = kills;
        this.rewardPointsCollected = rewards;
        this.buildingsBuilt = buildingsBuilt;
    }

    public int getBuildingsBuilt() {
        return buildingsBuilt;
    }
}
