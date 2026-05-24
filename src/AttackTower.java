import java.awt.Color;
import java.util.List;

/** Shared targeting and cooldown logic for attack towers. */
public abstract class AttackTower extends Building {
    protected final int damage;
    protected final double attackInterval;
    protected double cooldown;

    protected AttackTower(GridPosition position, int maxHp, int cost, int range,
            BuildingType type, int damage, double attackInterval) {
        super(position, maxHp, cost, range, type, false);
        this.damage = damage;
        this.attackInterval = attackInterval;
    }

    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        cooldown = Math.max(0.0, cooldown - dt);
        if (canAttack()) {
            attack(enemies, projectiles, map);
        }
    }

    protected void attack(List<Enemy> enemies, ProjectileManager projectiles, GridMap map) {
        Enemy target = findClosestEnemy(enemies);
        if (target == null) {
            return;
        }
        target.takeDamage(damage);
        projectiles.addProjectile(position, target.getGridPosition(), Color.YELLOW);
        resetCooldown();
        playShootSound();
    }

    protected void playShootSound() {
        SoundManager sm = SoundManager.getInstance();
        if (sm == null) return;
        switch (getType()) {
            case ARROW_TOWER: sm.playArrowShoot(); break;
            case CANNON_TOWER: sm.playCannonShoot(); break;
            case ICE_TOWER: sm.playIceShoot(); break;
            case LIGHTNING_TOWER: sm.playLightningShoot(); break;
        }
    }

    protected Enemy findClosestEnemy(List<Enemy> enemies) {
        Enemy closest = null;
        double bestDistance = Double.MAX_VALUE;
        for (Enemy enemy : enemies) {
            double distance = distanceTo(enemy.getGridPosition());
            if (!enemy.isDead() && distance <= range && distance < bestDistance) {
                closest = enemy;
                bestDistance = distance;
            }
        }
        return closest;
    }

    protected boolean isEnemyInRange(Enemy enemy) {
        return !enemy.isDead() && distanceTo(enemy.getGridPosition()) <= range;
    }

    protected double distanceTo(GridPosition other) {
        int rowDiff = position.row - other.row;
        int colDiff = position.col - other.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }

    protected boolean canAttack() {
        return cooldown <= 0.0;
    }

    protected void resetCooldown() {
        cooldown = attackInterval;
    }
}
