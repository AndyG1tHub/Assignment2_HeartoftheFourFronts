package enemy;

import core.GridPosition;
import enemy.enemies.ArcherEnemy;
import enemy.enemies.AssassinEnemy;
import enemy.enemies.EliteEnemy;
import enemy.enemies.FinalBossEnemy;
import enemy.enemies.HealerEnemy;
import enemy.enemies.MeleeEnemy;
import enemy.enemies.TankEnemy;
import manager.DifficultyManager;

/** Creates enemies with difficulty-scaled stats. */
public class EnemyFactory {
    private final DifficultyManager difficultyManager;

    public EnemyFactory(DifficultyManager difficultyManager) {
        this.difficultyManager = difficultyManager;
    }

    public Enemy createEnemy(EnemyType type, GridPosition position) {
        if (type == EnemyType.TANK) {
            return new TankEnemy(position, scaleHp(TankEnemy.BASE_HP));
        }
        if (type == EnemyType.ASSASSIN) {
            return new AssassinEnemy(position, scaleHp(AssassinEnemy.BASE_HP));
        }
        if (type == EnemyType.ARCHER) {
            return new ArcherEnemy(position, scaleHp(ArcherEnemy.BASE_HP));
        }
        if (type == EnemyType.HEALER) {
            return new HealerEnemy(position, scaleHp(HealerEnemy.BASE_HP));
        }
        if (type == EnemyType.ELITE) {
            return new EliteEnemy(position, scaleHp(EliteEnemy.BASE_HP));
        }
        if (type == EnemyType.FINAL_BOSS) {
            return new FinalBossEnemy(position, scaleHp(FinalBossEnemy.BASE_HP));
        }
        return new MeleeEnemy(position, scaleHp(MeleeEnemy.BASE_HP));
    }

    private int scaleHp(int baseHp) {
        return (int) Math.round(baseHp * difficultyManager.getEnemyHpMultiplier());
    }
}
