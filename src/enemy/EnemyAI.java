package enemy;

import core.GridPosition;
import core.GridMap;
import core.PathFinder;
import building.Base;
import building.Decoy;
import manager.DecoyManager;

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
        if (isInBaseAttackRange(enemy)) {
            enemy.stopAtCurrentTile(map);
            enemy.attackBase(dt, base);
            return;
        }
        enemy.followPath(pathFinder.findPath(map, enemy.getGridPosition(), base.getPosition()), dt, map);
    }

    private boolean isInBaseAttackRange(Enemy enemy) {
        GridPosition enemyPosition = enemy.getGridPosition();
        GridPosition basePosition = base.getPosition();
        int rowDiff = enemyPosition.row - basePosition.row;
        int colDiff = enemyPosition.col - basePosition.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= enemy.getBaseAttackRange();
    }

}
