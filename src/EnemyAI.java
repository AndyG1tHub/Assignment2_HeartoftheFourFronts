import java.util.List;

/** Handles enemy decisions while Enemy keeps movement and combat mechanics. */
public class EnemyAI {
    private final GridMap map;
    private final Base base;
    private final PathFinder pathFinder;
    private DecoyManager decoyManager;

    public EnemyAI(GridMap map, Base base, PathFinder pathFinder) {
        this.map = map;
        this.base = base;
        this.pathFinder = pathFinder;
    }

    public void setDecoyManager(DecoyManager decoyManager) {
        this.decoyManager = decoyManager;
    }

    public void updateEnemyBehaviour(Enemy enemy, double dt) {
        if (enemy.isDead()) {
            return;
        }
        Decoy decoy = chooseTargetDecoy(enemy);
        if (decoy != null) {
            enemy.chaseDecoy(dt, decoy, map);
            return;
        }
        moveOrAttackBase(enemy, dt);
    }

    private Decoy chooseTargetDecoy(Enemy enemy) {
        if (decoyManager == null) {
            return null;
        }
        return decoyManager.findAttractingDecoy(enemy.getGridPosition());
    }

    private void moveOrAttackBase(Enemy enemy, double dt) {
        if (enemy.getGridPosition().equals(base.getPosition())) {
            enemy.attackBase(dt, base);
            return;
        }
        ensurePath(enemy, base.getPosition());
        enemy.followPath(dt, map);
    }

    private void ensurePath(Enemy enemy, GridPosition target) {
        if (!enemy.needsPathTo(target)) {
            return;
        }
        List<GridPosition> path = pathFinder.findPath(map, enemy.getGridPosition(), target);
        enemy.setPath(path);
    }
}
