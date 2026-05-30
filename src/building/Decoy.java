/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Moving lure that travels outward from the base in a cardinal direction.
 * Attracts nearby enemies within a 4-tile radius, overriding their AI.
 * Has its own HP pool and self-destructs off-map or at zero HP.
 * Renders as a yellow bait sprite with a direction arrow and range circle.
 */
package building;

import java.awt.Color;
import java.awt.Image;

import core.GridPosition;
import core.GridMap;
import util.Direction;
import game.GameConfig;
import game.GameEngine;
import manager.ImageManager;

/** Moving lure that travels outward and attracts nearby enemies. */
public class Decoy {
    private GridPosition gridPosition;
    private final Direction direction;
    private int hp;
    private double moveTimer;

    public Decoy(GridPosition start, Direction direction) {
        this.gridPosition = start;
        this.direction = direction;
        this.hp = 100;
    }

    public void update(double dt) {
        moveTimer += dt;
        if (moveTimer >= 1.2) {
            moveOneTile();
            moveTimer = 0.0;
        }
    }

    private void moveOneTile() {
        if (direction == Direction.NORTH) {
            gridPosition = gridPosition.add(-1, 0);
        } else if (direction == Direction.SOUTH) {
            gridPosition = gridPosition.add(1, 0);
        } else if (direction == Direction.WEST) {
            gridPosition = gridPosition.add(0, -1);
        } else {
            gridPosition = gridPosition.add(0, 1);
        }
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public boolean isOutside(GridMap map) {
        return !map.isInside(gridPosition);
    }

    public boolean attracts(GridPosition enemyPosition) {
        int rowDiff = gridPosition.row - enemyPosition.row;
        int colDiff = gridPosition.col - enemyPosition.col;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff) <= 4.0;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public void draw(GameEngine engine, GridMap map) {
        if (isOutside(map)) {
            return;
        }
        double cx = map.tileCenterX(gridPosition);
        double cy = map.tileCenterY(gridPosition);
        int ts = GameConfig.TILE_SIZE;

        double rangePixels = 4.0 * ts;
        engine.changeColor(new Color(240, 220, 95, 40));
        engine.drawSolidCircle(cx, cy, rangePixels);
        engine.changeColor(new Color(240, 220, 95, 120));
        engine.drawCircle(cx, cy, rangePixels, 1);

        Image image = ImageManager.getBait();
        if (image != null) {
            int x = map.toScreenX(gridPosition.col);
            int y = map.toScreenY(gridPosition.row);
            engine.drawImage(image, x, y, ts, ts);
        } else {
            engine.changeColor(new Color(240, 220, 95));
            engine.drawSolidCircle(cx, cy, 9);
        }

        double arrowLen = ts * 0.6;
        double ax = cx, ay = cy;
        if (direction == Direction.NORTH) ay = cy - arrowLen;
        else if (direction == Direction.SOUTH) ay = cy + arrowLen;
        else if (direction == Direction.WEST) ax = cx - arrowLen;
        else ax = cx + arrowLen;
        engine.changeColor(new Color(255, 220, 80, 200));
        engine.drawLine(cx, cy, ax, ay, 2);
        double tip = 6;
        if (direction == Direction.NORTH) {
            engine.drawLine(ax, ay, ax - tip, ay + tip, 2);
            engine.drawLine(ax, ay, ax + tip, ay + tip, 2);
        } else if (direction == Direction.SOUTH) {
            engine.drawLine(ax, ay, ax - tip, ay - tip, 2);
            engine.drawLine(ax, ay, ax + tip, ay - tip, 2);
        } else if (direction == Direction.WEST) {
            engine.drawLine(ax, ay, ax + tip, ay - tip, 2);
            engine.drawLine(ax, ay, ax + tip, ay + tip, 2);
        } else {
            engine.drawLine(ax, ay, ax - tip, ay - tip, 2);
            engine.drawLine(ax, ay, ax - tip, ay + tip, 2);
        }
    }
}
