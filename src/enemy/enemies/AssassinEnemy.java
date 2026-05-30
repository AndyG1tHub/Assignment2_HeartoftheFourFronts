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
