package building.tower;

import java.awt.Color;
import java.awt.Image;

import building.BuildingType;
import core.GridPosition;
import core.GridMap;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManager;

/** Fast single-target starter tower. */
public class ArrowTower extends AttackTower {
    public ArrowTower(GridPosition position) {
        super(position, 120, GameConfig.ARROW_TOWER_COST, 4,
                BuildingType.ARROW_TOWER, 15, 2.0);
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManager.getArrowTower();
        if (image != null) {
            int size = (int) (GameConfig.TILE_SIZE * 1.4);
            int offset = (size - GameConfig.TILE_SIZE) / 2;
            engine.drawImage(image, x - offset, y - offset, size, size);
            drawHealthBar(engine, map);
            drawLevelIndicator(engine, map);
            return;
        }
        engine.changeColor(new Color(70, 170, 95));
        engine.drawSolidRectangle(x + 5, y + 5, GameConfig.TILE_SIZE - 10, GameConfig.TILE_SIZE - 10);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 9, y + 22, "A", "Arial", 15);
        drawHealthBar(engine, map);
        drawLevelIndicator(engine, map);
    }
}
