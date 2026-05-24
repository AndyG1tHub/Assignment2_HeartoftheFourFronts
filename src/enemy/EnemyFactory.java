package enemy;

import core.GridPosition;
import manager.DifficultyManager;

/** Creates enemies with difficulty-scaled stats. */
public class EnemyFactory {
    private final DifficultyManager difficultyManager;

    public EnemyFactory(DifficultyManager difficultyManager) {
        this.difficultyManager = difficultyManager;
    }

    public Enemy createEnemy(EnemyType type, GridPosition position) {
        int hp = (int) Math.round(baseHp(type) * difficultyManager.getEnemyHpMultiplier());
        return new Enemy(position, type, hp, baseDamage(type),
                baseSpeed(type), moneyReward(type), scoreReward(type));
    }

    private int baseHp(EnemyType type) {
        if (type == EnemyType.RAVAGER) {
            return 120;
        }
        if (type == EnemyType.STONEBREAKER) {
            return 180;
        }
        if (type == EnemyType.BOSS) {
            return 700;
        }
        return 70;
    }

    private int baseDamage(EnemyType type) {
        return type == EnemyType.BOSS ? 40 : 12;
    }

    private double baseSpeed(EnemyType type) {
        return type == EnemyType.STONEBREAKER ? 0.75 : 1.0;
    }

    private int moneyReward(EnemyType type) {
        return type == EnemyType.GOLD_WRAITH ? 35 : 15;
    }

    private int scoreReward(EnemyType type) {
        return type == EnemyType.BOSS ? 500 : 75;
    }
}
