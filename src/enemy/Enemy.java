/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

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
import manager.ImageManager;

/**
 * <p>Abstract base class for all enemy entities. Handles movement along the path, HP, damage, attack timers, state transitions and hit-stun animation.</p>
 */
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
    private double freezeTimer;
    private double speedBoostTimer;
    private double damageBoostTimer;
    private double animationTime;
    private double attackAnimationTimer;
    private boolean damageAnimation;
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

    /** Updates status timers and delegates movement decisions to EnemyAI. */
    public void update(double dt, EnemyAI ai) {
        updateStatusEffects(dt);
        attackAnimationTimer = Math.max(0.0, attackAnimationTimer - dt);
        if (isFrozen()) {
            return;
        }
        animationTime += dt;
        clearDeadAttackTarget();
        ai.updateEnemyBehaviour(this, dt);
    }

    public double getBaseAttackRange() {
        if (type == EnemyType.ARCHER) {
            return 0.0;
        }
        if (type == EnemyType.BOSS) {
            return 2.0;
        }
        return 1.0;
    }

    private void updateStatusEffects(double dt) {
        if (freezeTimer > 0) {
            freezeTimer = Math.max(0.0, freezeTimer - dt);
        }
        if (speedBoostTimer > 0) {
            speedBoostTimer -= dt;
        }
        if (damageBoostTimer > 0) {
            damageBoostTimer -= dt;
        }
    }

    /** Moves along the next step of a path. */
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

    /** Attacks the player base when cooldown allows. */
    public void attackBase(double dt, Base base) {
        moveTarget = null;
        attackTarget = null;
        attackCooldown -= dt;
        state = EnemyState.ATTACKING_BASE;
        if (attackCooldown <= 0.0) {
            base.takeDamage(getEffectiveDamage());
            damageAnimation = false;
            onAttackLands();
            attackAnimationTimer = 0.45;
            attackCooldown = 1.0;
        }
    }

    /** Attacks a nearby building when cooldown allows. */
    public void attackBuilding(double dt, Building building) {
        if (building == null || building.isDestroyed()) {
            attackTarget = null;
            return;
        }
        moveTarget = null;
        attackTarget = building;
        attackCooldown -= dt;
        state = EnemyState.ATTACKING_BUILDING;
        if (attackCooldown <= 0.0) {
            building.takeDamage(getEffectiveDamage());
            damageAnimation = false;
            onAttackLands();
            clearDeadAttackTarget();
            attackAnimationTimer = 0.45;
            attackCooldown = 1.0;
        }
    }

    protected void onAttackLands() {
    }

    protected void setAttackAnimationTimer(double time) {
        attackAnimationTimer = time;
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

    /** Applies damage and switches to dead state at zero HP. */
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        if (attackAnimationTimer <= 0) {
            damageAnimation = true;
            attackAnimationTimer = 0.35;
        }
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

    /** Freezes the enemy in place for the given duration. */
    public void applyFreeze(double duration) {
        freezeTimer = Math.max(freezeTimer, duration);
    }

    public boolean isDead() {
        return state == EnemyState.DEAD || hp <= 0;
    }

    /** Draws this enemy without stack separation offset. */
    public void draw(GameEngine engine, GridMap map) {
        draw(engine, map, 0.0, 0.0);
    }

    /** Draws this enemy with a visual offset for crowded tiles. */
    public void draw(GameEngine engine, GridMap map, double offsetX, double offsetY) {
        if (shouldSnapToTileForAnimation()) {
            x = map.tileCenterX(gridPosition);
            y = map.tileCenterY(gridPosition);
        }
        double drawX = x + offsetX;
        double drawY = y + offsetY;
        Image sprite = isPlayingAttackAnimation()
                ? ImageManager.getEnemyAttackSprite(type, animationTime, facingLeft)
                : ImageManager.getEnemySprite(type, animationTime, facingLeft);
        double drawMul = getDrawSizeMultiplier();
        if (sprite != null) {
            double size = GameConfig.TILE_SIZE * 1.25 * drawMul;
            engine.drawImage(sprite, drawX - size / 2, drawY - size / 2, size, size);
            drawFreezeEffect(engine, size, drawX, drawY);
            drawHealthBar(engine, drawX, drawY);
            return;
        }
        engine.changeColor(getColor());
        engine.drawSolidCircle(drawX, drawY, GameConfig.TILE_SIZE * 0.35 * drawMul);
        engine.changeColor(Color.WHITE);
        engine.drawText(drawX - 5, drawY + 5, getDrawLabel(), "Arial", 12);
        drawFreezeEffect(engine, GameConfig.TILE_SIZE * drawMul, drawX, drawY);
        drawHealthBar(engine, drawX, drawY);
    }

    private void drawFreezeEffect(GameEngine engine, double enemySize, double drawX, double drawY) {
        if (!isFrozen()) {
            return;
        }
        Image image = ImageManager.getIceFreezeEffect();
        if (image == null) {
            return;
        }
        double effectW = enemySize * 0.95;
        double effectH = enemySize * 0.95;
        double effectX = drawX - effectW / 2;
        double effectY = drawY - effectH * 0.20;
        engine.setAlpha(0.58f);
        engine.drawImage(image, effectX, effectY, effectW, effectH);
        engine.setAlpha(1.0f);
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

    private void drawHealthBar(GameEngine engine, double drawX, double drawY) {
        double drawMul = getDrawSizeMultiplier();
        double barW = 24 * drawMul;
        engine.changeColor(Color.RED);
        engine.drawSolidRectangle(drawX - barW / 2, drawY - 20 * drawMul, barW, 4);
        double ratio = maxHp == 0 ? 0.0 : (double) hp / maxHp;
        engine.changeColor(Color.GREEN);
        engine.drawSolidRectangle(drawX - barW / 2, drawY - 20 * drawMul, barW * ratio, 4);
    }

    private double getCurrentSpeed() {
        double mul = 1.0;
        if (speedBoostTimer > 0) mul *= 1.6;
        return speed * mul;
    }

    public boolean isFrozen() {
        return freezeTimer > 0.0;
    }

    private boolean isPlayingAttackAnimation() {
        return state == EnemyState.ATTACKING_BASE || state == EnemyState.ATTACKING_BUILDING || attackAnimationTimer > 0.0;
    }

    private boolean shouldSnapToTileForAnimation() {
        return isPlayingAttackAnimation() && (!damageAnimation || shouldSnapOnDamageAnimation());
    }

    protected boolean shouldSnapOnDamageAnimation() {
        return true;
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

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(maxHp, hp));
        if (this.hp <= 0) {
            state = EnemyState.DEAD;
        }
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

    public int getEffectiveDamage() {
        return damageBoostTimer > 0 ? (int)(damage * 1.5) : damage;
    }

    public void applySpeedBoost(double duration) {
        speedBoostTimer = Math.max(speedBoostTimer, duration);
    }

    public void applyDamageBoost(double duration) {
        damageBoostTimer = Math.max(damageBoostTimer, duration);
    }

    public boolean hasSpeedBoost() {
        return speedBoostTimer > 0;
    }

    public boolean hasDamageBoost() {
        return damageBoostTimer > 0;
    }
}
