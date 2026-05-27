package enemy;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import building.Base;
import building.Building;
import building.Decoy;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManger;

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
    private double attackCooldown;
    private GridPosition moveTarget;
    private Building attackTarget;
    private boolean slowed;
    private double slowTimer;
    private double animationTime;
    private double attackAnimationTimer;
    private boolean facingUp;
    private boolean facingDown;
    private boolean facingLeft;
    private boolean facingRight;

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
        this.facingDown = true;
        this.facingRight = true;
    }

    public void snapToMap(GridMap map) {
        x = map.tileCenterX(gridPosition);
        y = map.tileCenterY(gridPosition);
    }

    public void update(double dt, EnemyAI ai) {
        animationTime += dt;
        attackAnimationTimer = Math.max(0.0, attackAnimationTimer - dt);
        updateStatusEffects(dt);
        clearDeadAttackTarget();
        ai.updateEnemyBehaviour(this, dt);
    }

    public double getBaseAttackRange() {
        if (type == EnemyType.ARCHER) {
            return 3.0;
        }
        if (type == EnemyType.HEALER) {
            return 2.0;
        }
        return 1.0;
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

    public void followPath(List<GridPosition> path, double dt, GridMap map) {
        if (path == null || path.size() < 2) {
            return;
        }
        attackTarget = null;
        GridPosition next = path.get(1);
        if (moveTarget != null && !moveTarget.equals(next)) {
            returnToCurrentTile(dt, map);
            return;
        }
        if (moveTarget == null) {
            moveTarget = next;
        }
        moveToward(moveTarget, dt, map);
    }

    private void returnToCurrentTile(double dt, GridMap map) {
        double centerX = map.tileCenterX(gridPosition);
        double centerY = map.tileCenterY(gridPosition);
        double step = getCurrentSpeed() * GameConfig.TILE_SIZE * dt;
        moveAxis(centerX, centerY, step);
        if (Math.abs(x - centerX) < 1.0 && Math.abs(y - centerY) < 1.0) {
            x = centerX;
            y = centerY;
            moveTarget = null;
        }
    }

    private void moveToward(GridPosition next, double dt, GridMap map) {
        if (!isNextToCurrentTile(next)) {
            moveTarget = null;
            return;
        }
        updateFacing(next);
        double targetX = map.tileCenterX(next);
        double targetY = map.tileCenterY(next);
        double step = getCurrentSpeed() * GameConfig.TILE_SIZE * dt;
        moveAxis(targetX, targetY, step);
        if (Math.abs(x - targetX) < 1.0 && Math.abs(y - targetY) < 1.0) {
            arriveAt(next, map);
        }
    }

    private void moveAxis(double targetX, double targetY, double step) {
        if (Math.abs(targetX - x) > 0.0) {
            x = moveValue(x, targetX, step);
            return;
        }
        y = moveValue(y, targetY, step);
    }

    private double moveValue(double current, double target, double step) {
        if (Math.abs(target - current) <= step) {
            return target;
        }
        return current + Math.signum(target - current) * step;
    }

    private boolean isNextToCurrentTile(GridPosition next) {
        int rowDiff = Math.abs(next.row - gridPosition.row);
        int colDiff = Math.abs(next.col - gridPosition.col);
        return rowDiff + colDiff == 1;
    }

    private void arriveAt(GridPosition next, GridMap map) {
        updateFacing(next);
        gridPosition = next;
        x = map.tileCenterX(next);
        y = map.tileCenterY(next);
        moveTarget = null;
        state = EnemyState.MOVING;
    }

    private void updateFacing(GridPosition next) {
        facingUp = next.row < gridPosition.row;
        facingDown = next.row > gridPosition.row;
        if (next.col < gridPosition.col) {
            facingLeft = true;
            facingRight = false;
        } else if (next.col > gridPosition.col) {
            facingLeft = false;
            facingRight = true;
        }
    }

    public void attackBase(double dt, Base base) {
        moveTarget = null;
        attackTarget = null;
        attackCooldown -= dt;
        state = EnemyState.ATTACKING_BASE;
        if (attackCooldown <= 0.0) {
            base.takeDamage(damage);
            attackAnimationTimer = 0.45;
            attackCooldown = 1.0;
        }
    }

    public void attackBuilding(double dt, Building building) {
        if (building == null || building.isDestroyed()) {
            attackTarget = null;
            return;
        }
        moveTarget = null;
        attackTarget = building;
        attackCooldown -= dt;
        state = EnemyState.ATTACKING_BASE;
        if (attackCooldown <= 0.0) {
            building.takeDamage(damage);
            clearDeadAttackTarget();
            attackAnimationTimer = 0.45;
            attackCooldown = 1.0;
        }
    }

    private void clearDeadAttackTarget() {
        if (attackTarget != null && attackTarget.isDestroyed()) {
            attackTarget = null;
            state = EnemyState.MOVING;
        }
    }

    public void chaseDecoy(Decoy decoy) {
        if (decoy == null || decoy.isDestroyed()) {
            return;
        }
        state = EnemyState.CHASING_DECOY;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        attackAnimationTimer = 0.35;
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
        if (isPlayingAttackAnimation()) {
            x = map.tileCenterX(gridPosition);
            y = map.tileCenterY(gridPosition);
        }
        Image sprite = isPlayingAttackAnimation()
                ? ImageManger.getEnemyAttackSprite(type, animationTime, facingLeft)
                : ImageManger.getEnemySprite(type, animationTime, facingLeft);
        double drawMul = getDrawSizeMultiplier();
        if (sprite != null) {
            double size = GameConfig.TILE_SIZE * 1.25 * drawMul;
            engine.drawImage(sprite, x - size / 2, y - size / 2, size, size);
            drawHealthBar(engine);
            return;
        }
        engine.changeColor(getColor());
        engine.drawSolidCircle(x, y, GameConfig.TILE_SIZE * 0.35 * drawMul);
        engine.changeColor(Color.WHITE);
        engine.drawText(x - 5, y + 5, getDrawLabel(), "Arial", 12);
        drawHealthBar(engine);
    }

    protected double getDrawSizeMultiplier() {
        return 1.0;
    }

    protected Color getColor() {
        return new Color(205, 70, 80);
    }

    protected String getDrawLabel() {
        return "M";
    }

    private void drawHealthBar(GameEngine engine) {
        double drawMul = getDrawSizeMultiplier();
        double barW = 24 * drawMul;
        engine.changeColor(Color.RED);
        engine.drawSolidRectangle(x - barW / 2, y - 20 * drawMul, barW, 4);
        double ratio = maxHp == 0 ? 0.0 : (double) hp / maxHp;
        engine.changeColor(Color.GREEN);
        engine.drawSolidRectangle(x - barW / 2, y - 20 * drawMul, barW * ratio, 4);
    }

    private double getCurrentSpeed() {
        return slowed ? speed * 0.5 : speed;
    }

    private boolean isPlayingAttackAnimation() {
        return state == EnemyState.ATTACKING_BASE || attackAnimationTimer > 0.0;
    }

    public void stopAtCurrentTile(GridMap map) {
        x = map.tileCenterX(gridPosition);
        y = map.tileCenterY(gridPosition);
        moveTarget = null;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public boolean isFacingUp() {
        return facingUp;
    }

    public boolean isFacingDown() {
        return facingDown;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public boolean isFacingRight() {
        return facingRight;
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
