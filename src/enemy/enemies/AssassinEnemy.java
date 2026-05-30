/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Fast, fragile damage dealer that rushes the base.
 * 80 base HP, 22 damage, 1.5 speed (fastest non-boss).
 * IceTower freeze is especially effective against them. Purple colour, label "S".
 */
package enemy.enemies;

import java.awt.Color;

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/** Fast enemy with low health but high base damage. */
public class AssassinEnemy extends Enemy {
    public static final int BASE_HP = 80;

    public AssassinEnemy(GridPosition position, int hp) {
        super(position, EnemyType.ASSASSIN, hp, 22, 1.5, 40, 120);
    }

    @Override
    protected Color getColor() {
        return new Color(155, 65, 190);
    }

    @Override
    protected String getDrawLabel() {
        return "S";
    }
}
