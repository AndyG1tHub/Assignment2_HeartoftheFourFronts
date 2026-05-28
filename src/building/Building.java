package building;

import java.util.List;

import java.awt.Color;

import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import combat.ProjectileManager;
import game.GameEngine;
import game.GameConfig;

/** Base class for all one-tile player buildings. */
public abstract class Building {
    protected final GridPosition position;
    protected int hp;
    protected final int maxHp;
    protected final int cost;
    protected final int range;
    protected final BuildingType type;
    protected int upgradeLevel;

    protected Building(GridPosition position, int maxHp, int cost, int range,
            BuildingType type) {
        this.position = position;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.cost = cost;
        this.range = range;
        this.type = type;
        this.upgradeLevel = 0;
    }

    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        // Default buildings do nothing each frame.
    }

    public abstract void draw(GameEngine engine, GridMap map);

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public GridPosition getPosition() {
        return position;
    }

    public int getCost() {
        return cost;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getRange() {
        return range;
    }

    public BuildingType getType() {
        return type;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int level) {
        upgradeLevel = Math.min(level, GameConfig.MAX_UPGRADE_LEVEL);
    }

    public boolean canUpgrade() {
        return upgradeLevel < GameConfig.MAX_UPGRADE_LEVEL;
    }

    public int getUpgradeCost() {
        return (int) (cost * (upgradeLevel + 1) * GameConfig.UPGRADE_COST_MULTIPLIER);
    }

    public void upgrade() {
        if (canUpgrade()) {
            upgradeLevel++;
            hp = maxHp;
        }
    }

    protected void drawHealthBar(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col) + 4;
        int y = map.toScreenY(position.row) + GameConfig.TILE_SIZE - 6;
        double ratio = maxHp == 0 ? 0.0 : (double) hp / maxHp;
        engine.changeColor(java.awt.Color.RED);
        engine.drawSolidRectangle(x, y, GameConfig.TILE_SIZE - 8, 3);
        engine.changeColor(java.awt.Color.GREEN);
        engine.drawSolidRectangle(x, y, (GameConfig.TILE_SIZE - 8) * ratio, 3);
    }

    protected void drawLevelIndicator(GameEngine engine, GridMap map) {
        if (upgradeLevel == 0) return;
        int cx = map.tileCenterX(position);
        int cy = map.toScreenY(position.row) + 6;
        int chevCount = upgradeLevel;
        int totalW = chevCount * 12 - 4;
        int startX = cx - totalW / 2;
        for (int i = 0; i < chevCount; i++) {
            int x = startX + i * 12;
            engine.changeColor(new Color(255, 220, 80));
            engine.drawLine(x, cy + 6, x + 4, cy, 2);
            engine.drawLine(x + 4, cy, x + 8, cy + 6, 2);
        }
    }
}
