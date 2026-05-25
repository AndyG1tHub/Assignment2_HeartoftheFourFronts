package enemy;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import building.Base;
import building.Decoy;
import game.GameEngine;
import game.GameConfig;
import util.AssetManager;

/** Base enemy that owns stats and grid movement; EnemyAI owns decisions. */
public class Enemy {
    private GridPosition gridPosition;
    private double x;
    private double y;
    private int hp;
    private final int maxHp;
    private final int damage;
    private final double speed;
    private final int moneyReward;
    private final int scoreReward;
    private final EnemyType type;
    private EnemyState state;
    private List<GridPosition> path;
    private int pathIndex;
    private double attackCooldown;
    private boolean slowed;
    private double slowTimer;
    private double animationTime;
    private Decoy targetDecoy;

    public Enemy(GridPosition gridPosition, EnemyType type, int hp, int damage,
            double speed, int moneyReward, int scoreReward) {
        this.gridPosition = gridPosition;
        this.type = type;
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.speed = speed;
        this.moneyReward = moneyReward;
        this.scoreReward = scoreReward;
        this.state = EnemyState.SPAWNING;
        this.path = new ArrayList<GridPosition>();
    }

    public void snapToMap(GridMap map) {
        x = map.tileCenterX(gridPosition);
        y = map.tileCenterY(gridPosition);
    }

    public void update(double dt, EnemyAI ai) {
        animationTime += dt;
        updateStatusEffects(dt);
        ai.updateEnemyBehaviour(this, dt);
    }

    public void supportAllies(double dt, List<Enemy> enemies) {
    }

    public double getBaseAttackRange() {
        return 0.0;
    }

    private void updateStatusEffects(double dt) {
        if (!slowed) {
            return;
        }
        slowTimer -= dt;
        if (slowTimer <= 0.0) {
            slowed = false;
        }
    }

    public void followPath(double dt, GridMap map) {
        if (path == null || pathIndex >= path.size()) {
            return;
        }
        moveToward(path.get(pathIndex), dt, map);
    }

    private void moveToward(GridPosition next, double dt, GridMap map) {
        double targetX = map.tileCenterX(next);
        double targetY = map.tileCenterY(next);
        double distance = Math.hypot(targetX - x, targetY - y);
        if (distance <= 1.0) {
            arriveAt(next, map);
            return;
        }
        double step = getCurrentSpeed() * GameConfig.TILE_SIZE * dt;
        x += ((targetX - x) / distance) * step;
        y += ((targetY - y) / distance) * step;
    }

    private void arriveAt(GridPosition next, GridMap map) {
        gridPosition = next;
        x = map.tileCenterX(next);
        y = map.tileCenterY(next);
        pathIndex++;
        state = EnemyState.MOVING;
    }

    public void attackBase(double dt, Base base) {
        attackCooldown -= dt;
        state = EnemyState.ATTACKING_BASE;
        if (attackCooldown <= 0.0) {
            base.takeDamage(damage);
            attackCooldown = 1.0;
        }
    }

    public void chaseDecoy(double dt, Decoy decoy, GridMap map) {
        if (decoy == null || decoy.isDestroyed()) {
            targetDecoy = null;
            return;
        }
        state = EnemyState.CHASING_DECOY;
        moveToward(decoy.getGridPosition(), dt, map);
    }

    public void setPath(List<GridPosition> newPath) {
        path = newPath == null ? new ArrayList<GridPosition>() : newPath;
        pathIndex = path.size() > 1 ? 1 : 0;
    }

    public boolean needsPathTo(GridPosition target) {
        return path == null || pathIndex >= path.size() || !path.get(path.size() - 1).equals(target);
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        if (hp <= 0) {
            state = EnemyState.DEAD;
        }
    }

    public void heal(int amount) {
        if (isDead()) {
            return;
        }
        hp = Math.min(maxHp, hp + amount);
    }

    public void applySlow(double duration) {
        slowed = true;
        slowTimer = Math.max(slowTimer, duration);
    }

    public boolean isDead() {
        return state == EnemyState.DEAD || hp <= 0;
    }

    public void draw(GameEngine engine, GridMap map) {
        Image sprite = AssetManager.getEnemySprite(type, animationTime);
        if (sprite != null) {
            double size = GameConfig.TILE_SIZE * 1.25;
            engine.drawImage(sprite, x - size / 2, y - size / 2, size, size);
            drawHealthBar(engine);
            return;
        }
        engine.changeColor(getColor());
        engine.drawSolidCircle(x, y, GameConfig.TILE_SIZE * 0.35);
        engine.changeColor(Color.WHITE);
        engine.drawText(x - 5, y + 5, getDrawLabel(), "Arial", 12);
        drawHealthBar(engine);
    }

    protected Color getColor() {
        return new Color(205, 70, 80);
    }

    protected String getDrawLabel() {
        return "M";
    }

    private void drawHealthBar(GameEngine engine) {
        double ratio = maxHp == 0 ? 0.0 : (double) hp / maxHp;
        engine.changeColor(Color.RED);
        engine.drawSolidRectangle(x - 12, y - 18, 24, 3);
        engine.changeColor(Color.GREEN);
        engine.drawSolidRectangle(x - 12, y - 18, 24 * ratio, 3);
    }

    private double getCurrentSpeed() {
        return slowed ? speed * 0.5 : speed;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public EnemyType getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMoneyReward() {
        return moneyReward;
    }

    public int getScoreReward() {
        return scoreReward;
    }
}
