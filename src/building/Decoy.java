package building;

import java.awt.Color;

import core.GridPosition;
import core.GridMap;
import util.Direction;
import game.GameEngine;

/** Moving lure that travels outward and attracts nearby enemies. */
public class Decoy {
    private GridPosition gridPosition;
    private final Direction direction;
    private int hp;
    private double moveTimer;

    public Decoy(GridPosition start, Direction direction) {
        this.gridPosition = start;
        this.direction = direction;
        this.hp = 80;
    }

    public void update(double dt) {
        moveTimer += dt;
        if (moveTimer >= 0.5) {
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
        engine.changeColor(new Color(240, 220, 95));
        engine.drawSolidCircle(map.tileCenterX(gridPosition), map.tileCenterY(gridPosition), 9);
    }
}
