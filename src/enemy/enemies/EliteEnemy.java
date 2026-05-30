/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package enemy.enemies;

import java.awt.Color;

import enemy.Enemy;
import enemy.EnemyType;
import core.GridPosition;
import game.GameConfig;

/**
 * <p>An elite enemy variant that appears periodically throughout the game. Has boosted HP and damage compared to standard enemies and grants greater rewards upon defeat.</p>
 */
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
