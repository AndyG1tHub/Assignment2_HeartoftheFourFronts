package core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.GameConfig;
import game.GameEngine;
import building.Building;

/** Stores and draws the 20x20 grid and validates placement/movement rules. */
public class GridMap {
    private final Tile[][] tiles;
    private final GridPosition basePosition;

    public GridMap() {
        tiles = new Tile[GameConfig.GRID_ROWS][GameConfig.GRID_COLS];
        basePosition = new GridPosition(GameConfig.GRID_ROWS / 2, GameConfig.GRID_COLS / 2);
        createEmptyTiles();
        placeCentralBase();
        generateSimpleObstacles();
    }

    private void createEmptyTiles() {
        for (int row = 0; row < GameConfig.GRID_ROWS; row++) {
            for (int col = 0; col < GameConfig.GRID_COLS; col++) {
                tiles[row][col] = new Tile(row, col, TileType.EMPTY);
            }
        }
    }

    private void placeCentralBase() {
        getTile(basePosition).setType(TileType.BASE);
    }

    private void generateSimpleObstacles() {
        // TODO: replace with a stronger obstacle generator after pathing tests.
        setObstacle(4, 4);
        setObstacle(4, 5);
        setObstacle(15, 15);
        setObstacle(15, 14);
    }

    private void setObstacle(int row, int col) {
        if (isInside(row, col)) {
            tiles[row][col].setType(TileType.OBSTACLE);
        }
    }

    public boolean isInside(GridPosition position) {
        return position != null && isInside(position.row, position.col);
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < GameConfig.GRID_ROWS && col >= 0 && col < GameConfig.GRID_COLS;
    }

    public boolean isWalkable(GridPosition position) {
        return isInside(position) && getTile(position).isWalkable();
    }

    public boolean isBuildable(GridPosition position) {
        return isInside(position) && getTile(position).isBuildable();
    }

    public boolean placeBuilding(Building building) {
        if (building == null || !isBuildable(building.getPosition())) {
            return false;
        }
        getTile(building.getPosition()).setBuilding(building);
        return true;
    }

    public void removeBuilding(GridPosition position) {
        if (isInside(position)) {
            getTile(position).setBuilding(null);
        }
    }

    public Building getBuildingAt(GridPosition position) {
        return isInside(position) ? getTile(position).getBuilding() : null;
    }

    public Tile getTile(GridPosition position) {
        return tiles[position.row][position.col];
    }

    public GridPosition getBasePosition() {
        return basePosition;
    }

    public GridPosition mouseToGrid(int mouseX, int mouseY) {
        int col = (mouseX - GameConfig.MAP_OFFSET_X) / GameConfig.TILE_SIZE;
        int row = (mouseY - GameConfig.MAP_OFFSET_Y) / GameConfig.TILE_SIZE;
        GridPosition position = new GridPosition(row, col);
        return isInside(position) ? position : null;
    }

    public List<GridPosition> getAllEmptyPositions() {
        List<GridPosition> positions = new ArrayList<GridPosition>();
        for (int row = 0; row < GameConfig.GRID_ROWS; row++) {
            for (int col = 0; col < GameConfig.GRID_COLS; col++) {
                GridPosition position = new GridPosition(row, col);
                if (isBuildable(position)) {
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    public void draw(GameEngine engine) {
        drawTiles(engine);
        drawGridLines(engine);
    }

    private void drawTiles(GameEngine engine) {
        for (int row = 0; row < GameConfig.GRID_ROWS; row++) {
            for (int col = 0; col < GameConfig.GRID_COLS; col++) {
                drawTile(engine, tiles[row][col]);
            }
        }
    }

    private void drawTile(GameEngine engine, Tile tile) {
        GridPosition position = tile.getPosition();
        int x = toScreenX(position.col);
        int y = toScreenY(position.row);
        engine.changeColor(getTileColor(tile));
        engine.drawSolidRectangle(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
    }

    private Color getTileColor(Tile tile) {
        if (tile.getType() == TileType.BASE) {
            return new Color(80, 120, 210);
        }
        if (tile.getType() == TileType.OBSTACLE) {
            return new Color(80, 80, 80);
        }
        return new Color(45, 55, 50);
    }

    private void drawGridLines(GameEngine engine) {
        engine.changeColor(new Color(95, 105, 100));
        int width = GameConfig.GRID_COLS * GameConfig.TILE_SIZE;
        int height = GameConfig.GRID_ROWS * GameConfig.TILE_SIZE;
        for (int row = 0; row <= GameConfig.GRID_ROWS; row++) {
            int y = toScreenY(row);
            engine.drawLine(GameConfig.MAP_OFFSET_X, y, GameConfig.MAP_OFFSET_X + width, y);
        }
        for (int col = 0; col <= GameConfig.GRID_COLS; col++) {
            int x = toScreenX(col);
            engine.drawLine(x, GameConfig.MAP_OFFSET_Y, x, GameConfig.MAP_OFFSET_Y + height);
        }
    }

    public int toScreenX(int col) {
        return GameConfig.MAP_OFFSET_X + col * GameConfig.TILE_SIZE;
    }

    public int toScreenY(int row) {
        return GameConfig.MAP_OFFSET_Y + row * GameConfig.TILE_SIZE;
    }

    public int tileCenterX(GridPosition position) {
        return toScreenX(position.col) + GameConfig.TILE_SIZE / 2;
    }

    public int tileCenterY(GridPosition position) {
        return toScreenY(position.row) + GameConfig.TILE_SIZE / 2;
    }
}
