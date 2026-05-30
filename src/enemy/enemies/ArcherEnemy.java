/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Ranged enemy that attacks from 2 tiles away.
 * 70 base HP, 15 damage, 1.1 speed, high rewards (110 score, 30 money).
 * Green colour, label "R" for ranged.
 */
package enemy.enemies;

import java.awt.Color;

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/** Ranged-role enemy tuned as a fragile, rewarding damage dealer. */
public class ArcherEnemy extends Enemy {
    public static final int BASE_HP = 70;

    public ArcherEnemy(GridPosition position, int hp) {
        super(position, EnemyType.ARCHER, hp, 15, 1.1, 30, 110);
    }

    @Override
    public double getBaseAttackRange() {
        return 2.0;
    }

    @Override
    protected Color getColor() {
        return new Color(70, 145, 95);
    }

    @Override
    protected String getDrawLabel() {
        return "R";
    }
}
