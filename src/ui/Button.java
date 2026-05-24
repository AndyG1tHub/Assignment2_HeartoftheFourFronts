package ui;

import java.awt.Color;

import building.BuildingType;
import game.GameEngine;

/** Simple rectangle UI button with an optional building type payload. */
public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String text;
    private final BuildingType buildingType;

    public Button(int x, int y, int width, int height, String text, BuildingType buildingType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.buildingType = buildingType;
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void draw(GameEngine engine, boolean selected) {
        engine.changeColor(selected ? new Color(80, 130, 210) : new Color(55, 60, 65));
        engine.drawSolidRectangle(x, y, width, height);
        engine.changeColor(Color.WHITE);
        engine.drawRectangle(x, y, width, height);
        engine.drawText(x + 8, y + 21, text, "Arial", 13);
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
}
