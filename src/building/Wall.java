package building;

import java.awt.Color;

import core.GridPosition;
import core.GridMap;
import game.GameEngine;
import game.GameConfig;

/** High-health blocking defence. */
public class Wall extends Building {
    public Wall(GridPosition position) {
        super(position, 350, GameConfig.WALL_COST, 0, BuildingType.WALL);
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        engine.changeColor(new Color(150, 150, 150));
        engine.drawSolidRectangle(x + 3, y + 3, GameConfig.TILE_SIZE - 6, GameConfig.TILE_SIZE - 6);
        engine.changeColor(Color.DARK_GRAY);
        engine.drawRectangle(x + 6, y + 6, GameConfig.TILE_SIZE - 12, GameConfig.TILE_SIZE - 12);
        drawHealthBar(engine, map);
    }
}
