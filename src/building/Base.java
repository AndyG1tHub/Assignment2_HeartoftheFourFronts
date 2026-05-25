package building;

import java.awt.Color;
import java.awt.Image;

import core.GridPosition;
import core.GridMap;
import game.GameEngine;
import game.GameConfig;
import manager.ImageManger;
import manager.SoundManager;

/** The central objective enemies try to destroy. */
public class Base {
    private final GridPosition position;
    private final int maxHp;
    private int hp;

    public Base(GridPosition position) {
        this.position = position;
        maxHp = GameConfig.BASE_MAX_HP;
        hp = maxHp;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playBaseHit();
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public GridPosition getPosition() {
        return position;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void draw(GameEngine engine, GridMap map) {
        int x = map.toScreenX(position.col);
        int y = map.toScreenY(position.row);
        Image image = ImageManger.getBaseImage();
        if (image != null) {
            int size = GameConfig.TILE_SIZE * 2;
            int centerX = map.tileCenterX(position);
            int centerY = map.tileCenterY(position);
            engine.drawImage(image, centerX - size / 2, centerY - size / 2,
                    size, size);
            return;
        }
        engine.changeColor(new Color(70, 125, 230));
        engine.drawSolidRectangle(x + 3, y + 3, GameConfig.TILE_SIZE - 6, GameConfig.TILE_SIZE - 6);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 8, y + 22, "B", "Arial", 16);
    }
}
