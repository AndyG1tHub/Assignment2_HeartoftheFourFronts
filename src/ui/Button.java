/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Reusable UI button with hover, selected, and normal rendering states.
 * Optionally holds a BuildingType for build menu and an accent colour bar.
 * Repositioned and resized each frame via setPosition/setSize.
 * Click detection uses simple axis-aligned bounds checking.
 */
package ui;

import java.awt.Color;

import building.BuildingType;
import game.GameEngine;

/** UI button with optional type-specific color and selected state. */
public class Button {
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
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
        draw(engine, selected, -1, -1);
    }

    public void draw(GameEngine engine, boolean selected, int mouseX, int mouseY) {
        boolean hovered = contains(mouseX, mouseY);

        if (selected) {
            engine.changeColor(new Color(55, 65, 80));
            engine.drawSolidRectangle(x, y, width, height);
        } else if (hovered) {
            engine.changeColor(new Color(45, 52, 60));
            engine.drawSolidRectangle(x, y, width, height);
        } else {
            engine.changeColor(BG);
            engine.drawSolidRectangle(x, y, width, height);
        }
        if (color != null) {
            engine.changeColor(hovered ? color.brighter() : color);
            engine.drawSolidRectangle(x + 4, y + 4, 6, height - 8);
        }
        if (selected) {
            engine.changeColor(SELECTED_BORDER);
            engine.drawRectangle(x - 1, y - 1, width + 2, height + 2, 2);
        } else if (hovered) {
            engine.changeColor(HOVER_BORDER);
            engine.drawRectangle(x, y, width, height);
        } else {
            engine.changeColor(BORDER);
            engine.drawRectangle(x, y, width, height);
        }
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 16, y + 21, text, "Arial", 13);
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getText() { return text; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public Color getColor() {
        return color;
    }
}
