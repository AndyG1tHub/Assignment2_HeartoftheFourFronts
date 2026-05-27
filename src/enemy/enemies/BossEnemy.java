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
                GameConfig.BOSS_SPEED, 100, 500);
    }

    @Override
    protected Color getColor() {
        return new Color(180, 40, 200);
    }

    @Override
    protected String getDrawLabel() {
        return "B";
    }
}
