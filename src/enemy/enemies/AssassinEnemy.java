package enemy.enemies;

import java.awt.Color;

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/** Fast enemy with low health but high base damage. */
public class AssassinEnemy extends Enemy {
    public static final int BASE_HP = 60;

    public AssassinEnemy(GridPosition position, int hp) {
        super(position, EnemyType.ASSASSIN, hp, 28, 1.55, 28, 90);
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
