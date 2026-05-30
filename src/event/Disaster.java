/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Base class for timed natural disasters on the grid.
 * Stores position, radius, and damage for area effects.
 * Subclasses implement applyEffect() to define what happens on trigger.
 * affects() checks whether a grid position falls within the radius.
 * drawArea() renders a bounding rectangle for the affected zone.
 */
package event;

import java.awt.Color;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import building.Building;
import game.GameEngine;
import game.GameConfig;

/** Base class for timed natural disasters. */
public abstract class Disaster extends GameEvent {
    protected final GridPosition position;
    protected final int radius;
    protected final int damage;

    protected Disaster(EventType type, GridPosition position, int radius, int damage, double duration) {
        super(type, duration);
        this.position = position;
        this.radius = radius;
        this.damage = damage;
    }

    public abstract void applyEffect(List<Enemy> enemies, List<Building> buildings);

    protected boolean affects(GridPosition other) {
        int rowDiff = Math.abs(position.row - other.row);
        int colDiff = Math.abs(position.col - other.col);
        return rowDiff <= radius && colDiff <= radius;
    }

    protected void drawArea(GameEngine engine, GridMap map, Color color) {
        engine.changeColor(color);
        int size = (radius * 2 + 1) * GameConfig.TILE_SIZE;
        int x = map.tileCenterX(position) - size / 2;
        int y = map.tileCenterY(position) - size / 2;
        engine.drawRectangle(x, y, size, size, 2);
    }
}
