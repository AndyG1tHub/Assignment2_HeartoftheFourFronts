package ui;

import java.awt.Color;

import building.BuildingType;
import game.GameEngine;

/** UI button with optional type-specific color and selected state. */
public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String text;
    private final BuildingType buildingType;
    private final Color color;

    private static final Color BG = new Color(35, 40, 45);
    private static final Color BORDER = new Color(80, 85, 90);
    private static final Color SELECTED_BORDER = new Color(200, 180, 80);
    private static final Color HOVER_BORDER = new Color(130, 135, 140);

    public Button(int x, int y, int width, int height, String text, BuildingType buildingType) {
        this(x, y, width, height, text, buildingType, null);
    }

    public Button(int x, int y, int width, int height, String text, BuildingType buildingType, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.buildingType = buildingType;
        this.color = color;
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void draw(GameEngine engine, boolean selected) {
        if (selected) {
            engine.changeColor(new Color(55, 65, 80));
            engine.drawSolidRectangle(x, y, width, height);
        } else {
            engine.changeColor(BG);
            engine.drawSolidRectangle(x, y, width, height);
        }
        if (color != null) {
            engine.changeColor(color);
            engine.drawSolidRectangle(x + 4, y + 4, 6, height - 8);
        }
        if (selected) {
            engine.changeColor(SELECTED_BORDER);
            engine.drawRectangle(x - 1, y - 1, width + 2, height + 2, 2);
        } else {
            engine.changeColor(BORDER);
            engine.drawRectangle(x, y, width, height);
        }
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 16, y + 21, text, "Arial", 13);
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
}
