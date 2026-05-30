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

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/**
 * <p>A slow, high-HP armoured enemy that absorbs tower fire. Designed to soak up damage while other enemies advance.</p>
 */
public class TankEnemy extends Enemy {
    public static final int BASE_HP = 250;

    public TankEnemy(GridPosition position, int hp) {
        super(position, EnemyType.TANK, hp, 20, 0.7, 35, 150);
    }

    @Override
    protected Color getColor() {
        return new Color(95, 105, 115);
    }

    @Override
    protected String getDrawLabel() {
        return "T";
    }
}
