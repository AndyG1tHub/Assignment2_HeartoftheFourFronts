package enemy.enemies;

import java.awt.Color;
import java.util.List;

import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;

/** Support enemy that periodically restores nearby damaged enemies. */
public class HealerEnemy extends Enemy {
    public static final int BASE_HP = 90;
    private static final int HEAL_AMOUNT = 10;
    private static final double HEAL_INTERVAL = 2.0;
    private static final double HEAL_RANGE = 2.5;

    private double healCooldown;

    public HealerEnemy(GridPosition position, int hp) {
        super(position, EnemyType.HEALER, hp, 8, 0.9, 22, 110);
    }

    @Override
    public void supportAllies(double dt, List<Enemy> enemies) {
        healCooldown = Math.max(0.0, healCooldown - dt);
        if (healCooldown > 0.0 || isDead()) {
            return;
        }
        for (Enemy enemy : enemies) {
            if (enemy != this && canHeal(enemy)) {
                enemy.heal(HEAL_AMOUNT);
                healCooldown = HEAL_INTERVAL;
                return;
            }
        }
    }

    private boolean canHeal(Enemy enemy) {
        return !enemy.isDead()
                && enemy.getHp() < enemy.getMaxHp()
                && distanceTo(enemy.getGridPosition()) <= HEAL_RANGE;
    }

    private double distanceTo(GridPosition other) {
        int rowDiff = getGridPosition().row - other.row;
        int colDiff = getGridPosition().col - other.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }

    @Override
    protected Color getColor() {
        return new Color(85, 185, 165);
    }

    @Override
    protected String getDrawLabel() {
        return "H";
    }
}
