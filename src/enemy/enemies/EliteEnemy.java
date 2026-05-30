/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Mini-boss used as final wave in levels 1-2 and elite waves throughout.
 * 500 base HP, 35 damage, 0.9 speed, 1.5x draw scale.
 * Replaces boss encounters when the full tower arsenal is not yet available.
 */
package enemy.enemies;

import java.awt.Color;

import enemy.Enemy;
import enemy.EnemyType;
import core.GridPosition;
import game.GameConfig;

public class EliteEnemy extends Enemy {
    public static final int BASE_HP = GameConfig.ELITE_BASE_HP;

    public EliteEnemy(GridPosition position, int hp) {
        super(position, EnemyType.ELITE, hp, GameConfig.ELITE_DAMAGE,
                GameConfig.ELITE_SPEED, 150, 400);
    }

    @Override
    protected double getDrawSizeMultiplier() {
        return 1.5;
    }

    @Override
    protected Color getColor() {
        return new Color(200, 100, 220);
    }

    @Override
    protected String getDrawLabel() {
        return "ELITE";
    }
}
