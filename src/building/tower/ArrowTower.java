package building.tower;

import java.awt.Color;
import java.awt.Image;

import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManger;

/** Fast single-target starter tower. */
public class ArrowTower extends AttackTower {
    public ArrowTower(GridPosition position) {
        super(position, 120, GameConfig.ARROW_TOWER_COST, 4,
                BuildingType.ARROW_TOWER, 15, 1.0);
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManger.getArrowTower();
        if (image != null) {
            engine.drawImage(image, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            drawHealthBar(engine, map);
            return;
        }
        engine.changeColor(new Color(70, 170, 95));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 9, y + 22, "A", "Arial", 15);
        drawHealthBar(engine, map);
    }
}
