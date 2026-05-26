package core;

import building.Building;

/** One map cell. It owns terrain type and an optional building reference. */
public class Tile {
    private final int row;
    private final int col;
    private TileType type;
    private Building building;

    public Tile(int row, int col, TileType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public boolean isWalkable() {
        if (type == TileType.OBSTACLE) {
            return false;
        }
        return building == null;
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
}
