/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package enemy;

import core.GridPosition;
import enemy.enemies.ArcherEnemy;
import enemy.enemies.AssassinEnemy;
import enemy.enemies.BossEnemy;
import enemy.enemies.EliteEnemy;
import enemy.enemies.MeleeEnemy;
import enemy.enemies.TankEnemy;
import manager.DifficultyManager;

/** Creates enemies with difficulty-scaled stats. */
public class EnemyFactory {
    private final DifficultyManager difficultyManager;

    public EnemyFactory(DifficultyManager difficultyManager) {
        this.difficultyManager = difficultyManager;
    }

    /** Creates an enemy with difficulty-scaled health. */
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
        if (type == EnemyType.BOSS) {
            return new BossEnemy(position, scaleHp(BossEnemy.BASE_HP));
        }
        if (type == EnemyType.ELITE) {
            return new EliteEnemy(position, scaleHp(EliteEnemy.BASE_HP));
        }
        return new MeleeEnemy(position, scaleHp(MeleeEnemy.BASE_HP));
    }

    /** Creates a boss with a pre-scaled health value. */
    public Enemy createBoss(GridPosition position, int hp) {
        return new BossEnemy(position, hp);
    }

    private int scaleHp(int baseHp) {
        return (int) Math.round(baseHp * difficultyManager.getEnemyHpMultiplier());
    }
}
