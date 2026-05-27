package enemy.enemies;

import java.awt.Color;
import java.util.List;

import enemy.Enemy;
import enemy.EnemyType;
import core.GridPosition;
import game.GameConfig;

public class FinalBossEnemy extends Enemy {
    public static final int BASE_HP = GameConfig.FINAL_BOSS_BASE_HP;
    private double skillTimer;
    private int skillIndex;

    public FinalBossEnemy(GridPosition position, int hp) {
        super(position, EnemyType.FINAL_BOSS, hp, GameConfig.FINAL_BOSS_DAMAGE,
                GameConfig.FINAL_BOSS_SPEED, 500, 2000);
    }

    @Override
    public void update(double dt, enemy.EnemyAI ai) {
        super.update(dt, ai);
        skillTimer += dt;
    }

    public void applySkills(List<Enemy> allEnemies) {
        if (skillTimer < 5.0) return;
        skillTimer = 0;
        skillIndex = (skillIndex + 1) % 3;
        for (Enemy e : allEnemies) {
            if (e == this || e.isDead()) continue;
            if (skillIndex == 0) {
                e.applySpeedBoost(5.0);
            } else if (skillIndex == 1) {
                e.heal(80);
            } else {
                e.applyDamageBoost(5.0);
            }
        }
    }

    @Override
    protected double getDrawSizeMultiplier() {
        return 2.0;
    }

    @Override
    protected Color getColor() {
        return new Color(180, 20, 40);
    }

    @Override
    protected String getDrawLabel() {
        return "FINAL BOSS";
    }
}
