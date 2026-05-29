package enemy.enemies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import building.Building;
import building.tower.AttackTower;
import core.GridPosition;
import enemy.Enemy;
import enemy.EnemyType;
import game.GameConfig;

/** Heavy enemy that damages and disables nearby attack towers when it attacks. */
public class BossEnemy extends Enemy {
    public static final int BASE_HP = GameConfig.BOSS_BASE_HP;
    private static final double TOWER_EFFECT_RANGE = 3.0;
    private static final double TOWER_DISABLE_DURATION = 2.0;
    private static final int TOWER_DAMAGE = 12;

    private List<Building> buildings;

    public BossEnemy(GridPosition position, int hp) {
        super(position, EnemyType.BOSS, hp, GameConfig.BOSS_DAMAGE,
                GameConfig.BOSS_SPEED, 150, 500);
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public double getBaseAttackRange() {
        return 2.0;
    }

    @Override
    protected void onAttackLands() {
        affectNearbyTowers();
    }

    private void affectNearbyTowers() {
        List<AttackTower> towers = findNearbyAttackTowers();
        int maxTargets = (int) Math.ceil(towers.size() / 3.0);
        for (int i = 0; i < maxTargets; i++) {
            AttackTower tower = towers.get(i);
            tower.disable(TOWER_DISABLE_DURATION);
            tower.takeDamage(TOWER_DAMAGE);
        }
    }

    private List<AttackTower> findNearbyAttackTowers() {
        List<AttackTower> towers = new ArrayList<AttackTower>();
        if (buildings == null) {
            return towers;
        }
        for (Building building : buildings) {
            if (building instanceof AttackTower
                    && !building.isDestroyed()
                    && distanceTo(building.getPosition()) <= TOWER_EFFECT_RANGE) {
                towers.add((AttackTower) building);
            }
        }
        Collections.sort(towers, new Comparator<AttackTower>() {
            public int compare(AttackTower first, AttackTower second) {
                return Double.compare(distanceTo(first.getPosition()), distanceTo(second.getPosition()));
            }
        });
        return towers;
    }

    private double distanceTo(GridPosition position) {
        int rowDiff = getGridPosition().row - position.row;
        int colDiff = getGridPosition().col - position.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }

    @Override
    protected double getDrawSizeMultiplier() {
        return 1.85;
    }

    @Override
    protected boolean shouldSnapOnDamageAnimation() {
        return false;
    }

    @Override
    protected Color getColor() {
        return new Color(120, 50, 175);
    }

    @Override
    protected String getDrawLabel() {
        return "B";
    }
}
