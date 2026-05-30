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

/** Standard frontline enemy with balanced health, damage, and speed. */
public class MeleeEnemy extends Enemy {
    public static final int BASE_HP = 100;

    public MeleeEnemy(GridPosition position, int hp) {
        super(position, EnemyType.MELEE, hp, 12, 1.0, 20, 80);
    }

    @Override
    protected Color getColor() {
        return new Color(205, 70, 80);
    }

    @Override
    protected String getDrawLabel() {
        return "M";
    }
}
