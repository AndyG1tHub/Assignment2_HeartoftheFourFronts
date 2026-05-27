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
                GameConfig.ELITE_SPEED, 100, 300);
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
