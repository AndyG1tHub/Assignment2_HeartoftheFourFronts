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

    public int getBuildingsBuilt() {
        return buildingsBuilt;
    }
}
