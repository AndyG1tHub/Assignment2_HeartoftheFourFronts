/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package core;

import building.Building;

/**
 * <p>A single tile on the game grid. Stores terrain type, obstacle flag, the building occupying it and references to neighbouring tiles.</p>
 */
public class Tile {
    private final int row;
    private final int col;
    private TileType type;
    private Building building;
    private int obstacleVariant;
    private boolean isFireObstacle;
    private int mapTileVariant;

    public Tile(int row, int col, TileType type) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.obstacleVariant = 0;
        this.isFireObstacle = false;
        this.mapTileVariant = 0;
    }

    public boolean isWalkable() {
        if (type == TileType.OBSTACLE) {
            return false;
        }
        return building == null || building.isDestroyed();
    }

    public boolean isWalkableIgnoringBuilding() {
        return type != TileType.OBSTACLE;
    }

    public boolean isBuildable() {
        return type == TileType.EMPTY && building == null;
    }

    public GridPosition getPosition() {
        return new GridPosition(row, col);
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
        this.type = building == null ? TileType.EMPTY : TileType.BUILDING;
    }

    public int getObstacleVariant() {
        return obstacleVariant;
    }

    public void setObstacleVariant(int variant) {
        this.obstacleVariant = variant;
    }

    public boolean isFireObstacle() {
        return isFireObstacle;
    }

    public void setFireObstacle(boolean isFireObstacle) {
        this.isFireObstacle = isFireObstacle;
    }

    public int getMapTileVariant() {
        return mapTileVariant;
    }

    public void setMapTileVariant(int variant) {
        this.mapTileVariant = variant;
    }
}
