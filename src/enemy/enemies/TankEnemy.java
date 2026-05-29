package enemy.enemies;

import java.awt.Color;

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/** Slow meat-shield enemy that survives longer and hits harder. */
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
