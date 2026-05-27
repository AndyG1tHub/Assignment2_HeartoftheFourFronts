package building.tower;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import building.Building;
import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import combat.ProjectileManager;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManger;
import manager.SoundManager;

/** Support building that heals nearby damaged buildings. */
public class HealTower extends Building {
    private static final double HEAL_INTERVAL = 2.0;
    private double cooldown;

    public HealTower(GridPosition position) {
        super(position, 120, GameConfig.HEAL_TOWER_COST, 3, BuildingType.HEAL_TOWER);
    }

    private int getHealAmount() {
        return GameConfig.HEAL_AMOUNT_UPGRADE[upgradeLevel];
    }

    @Override
    public void update(double dt, List<Enemy> enemies, ProjectileManager projectiles,
            GridMap map, List<Building> buildings) {
        cooldown = Math.max(0.0, cooldown - dt);
        if (cooldown <= 0.0) {
            healDamagedBuildings(buildings);
            cooldown = HEAL_INTERVAL;
        }
    }

    private void healDamagedBuildings(List<Building> buildings) {
        boolean healedAny = false;
        for (Building building : findDamagedBuildings(buildings)) {
            healBuilding(building);
            healedAny = true;
        }
        if (healedAny) {
            SoundManager sm = SoundManager.getInstance();
            if (sm != null) sm.playHealTower();
        }
    }

    private List<Building> findDamagedBuildings(List<Building> buildings) {
        List<Building> damaged = new ArrayList<Building>();
        for (Building building : buildings) {
            if (building != this && isInHealRange(building) && building.getHp() < building.getMaxHp()) {
                damaged.add(building);
            }
        }
        return damaged;
    }

    private void healBuilding(Building building) {
        building.heal(getHealAmount());
    }

    private boolean isInHealRange(Building building) {
        int rowDiff = position.row - building.getPosition().row;
        int colDiff = position.col - building.getPosition().col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= range;
    }

    public void drawRangeEffect(GameEngine engine, GridMap map) {
        Image rangeImage = ImageManger.getHealRangeEffect();
        if (rangeImage != null) {
            int size = (range * 2 + 1) * GameConfig.TILE_SIZE;
            engine.drawImage(rangeImage, map.tileCenterX(position) - size / 2,
                    map.tileCenterY(position) - size / 2, size, size);
        }
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManger.getHealTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
            drawHealthBar(engine, map);
            drawLevelIndicator(engine, map);
            return;
        }
        engine.changeColor(new Color(95, 205, 165));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 8, y + 22, "H", "Arial", 15);
        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
