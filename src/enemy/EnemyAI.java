/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package enemy;

import java.util.List;

import core.GridPosition;
import core.GridMap;
import core.PathFinder;
import building.Base;
import building.Building;
import building.BuildingType;
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

    /** Chooses whether an enemy chases, moves, or attacks this frame. */
    public void updateEnemyBehaviour(Enemy enemy, double dt) {
        if (enemy.isDead()) {
            return;
        }
        Decoy decoy = chooseTargetDecoy(enemy);
        if (decoy != null) {
            enemy.chaseDecoy(decoy);
            enemy.followPath(pathFinder.findPath(map, enemy.getGridPosition(), decoy.getGridPosition()), dt, map);
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

        // Check for nearby towers to attack before going to base
        Building nearbyTower = findNearbyTowerToAttack(enemy);
        if (nearbyTower != null) {
            enemy.stopAtCurrentTile(map);
            enemy.attackBuilding(dt, nearbyTower);
            return;
        }

        List<GridPosition> path = pathFinder.findPath(map, enemy.getGridPosition(), base.getPosition());
        if (!path.isEmpty()) {
            enemy.followPath(path, dt, map);
            return;
        }
        attackBlockingBuilding(enemy, dt);
    }

    private Building findNearbyTowerToAttack(Enemy enemy) {
        GridPosition enemyPos = enemy.getGridPosition();
        double attackRange = enemy.getBaseAttackRange();

        // Search in attack range around the enemy
        int range = (int)attackRange;
        for (int rowOffset = -range; rowOffset <= range; rowOffset++) {
            for (int colOffset = -range; colOffset <= range; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) continue;

                int checkRow = enemyPos.row + rowOffset;
                int checkCol = enemyPos.col + colOffset;

                if (!map.isInside(checkRow, checkCol)) continue;

                GridPosition checkPos = new GridPosition(checkRow, checkCol);
                Building building = map.getBuildingAt(checkPos);

                if (building != null && !building.isDestroyed() && isTower(building)) {
                    // Check if in attack range
                    if (isInAttackRange(enemy, checkPos, attackRange)) {
                        return building;
                    }
                }
            }
        }
        return null;
    }

    private boolean isTower(Building building) {
        BuildingType type = building.getType();
        return type == BuildingType.ARROW_TOWER ||
               type == BuildingType.CANNON_TOWER ||
               type == BuildingType.ICE_TOWER ||
               type == BuildingType.LIGHTNING_TOWER ||
               type == BuildingType.HEAL_TOWER;
    }

    private void attackBlockingBuilding(Enemy enemy, double dt) {
        List<GridPosition> blockedPath = pathFinder.findPathIgnoringBuildings(
                map, enemy.getGridPosition(), base.getPosition());
        GridPosition targetPosition = findFirstBuildingPosition(blockedPath);
        Building target = map.getBuildingAt(targetPosition);
        if (target == null || target.isDestroyed()) {
            return;
        }
        int targetIndex = blockedPath.indexOf(targetPosition);
        if (!isInAttackRange(enemy, targetPosition, 1.0) && targetIndex > 1) {
            enemy.followPath(blockedPath.subList(0, targetIndex), dt, map);
            return;
        }
        enemy.stopAtCurrentTile(map);
        enemy.attackBuilding(dt, target);
    }

    private GridPosition findFirstBuildingPosition(List<GridPosition> path) {
        for (GridPosition position : path) {
            Building building = map.getBuildingAt(position);
            if (building != null && !building.isDestroyed()) {
                return position;
            }
        }
        return null;
    }

    private boolean isInBaseAttackRange(Enemy enemy) {
        return isInAttackRange(enemy, base.getPosition(), enemy.getBaseAttackRange());
    }

    private boolean isInAttackRange(Enemy enemy, GridPosition target, double range) {
        GridPosition enemyPosition = enemy.getGridPosition();
        int rowDiff = Math.abs(enemyPosition.row - target.row);
        int colDiff = Math.abs(enemyPosition.col - target.col);
        return Math.max(rowDiff, colDiff) <= range;
    }

}
