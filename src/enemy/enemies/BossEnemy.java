package enemy.enemies;

import java.awt.Color;

import enemy.Enemy;
import enemy.EnemyType;
import core.GridPosition;
import game.GameConfig;

public class BossEnemy extends Enemy {
    public static final int BASE_HP = GameConfig.BOSS_BASE_HP;

    public BossEnemy(GridPosition position, int hp) {
        super(position, EnemyType.BOSS, hp, GameConfig.BOSS_DAMAGE,
                GameConfig.BOSS_SPEED, 150, 500);
    }

    @Override
    protected double getDrawSizeMultiplier() {
        return 1.8;
    }

    @Override
    protected Color getColor() {
        return new Color(200, 40, 220);
    }

    @Override
    protected String getDrawLabel() {
        return "BOSS";
    }
}
