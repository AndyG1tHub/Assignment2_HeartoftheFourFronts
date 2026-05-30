/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package ui;

import java.awt.Color;
import java.awt.Image;

import game.CoreSiege;
import game.GameConfig;
import game.GameState;
import manager.ImageManager;

public class IntroScreen {

    private String[] story = {
            "Long before the fall of kingdoms...",
            "The Four Fronts protected mankind.",
            "But war shattered the alliance.",
            "Darkness rises once again.",
            "Only one fortress still stands.",
            "You are the final commander.",
            "Hold the line."
    };

    private int currentLine = 0;
    private long lastSwitchTime;
    private final int DISPLAY_TIME = 3500;
    private Image backgroundImage;

    public IntroScreen() {
        lastSwitchTime = System.currentTimeMillis();
        backgroundImage = ImageManager.getBackground();
    }

    public void update(CoreSiege game) {
        long current = System.currentTimeMillis();
        if (current - lastSwitchTime > DISPLAY_TIME) {
            currentLine++;
            lastSwitchTime = current;
        }
        if (currentLine >= story.length) {
            game.setGameState(GameState.MENU);
        }
    }

    public void draw(CoreSiege game) {
        int w = GameConfig.WINDOW_WIDTH;
        int h = GameConfig.WINDOW_HEIGHT;

        game.drawImage(backgroundImage, 0, 0, w, h);

        if (currentLine < story.length) {
            int shadowOff = 4;
            int fontSize = currentLine >= 5 ? 46 : 36;
            String line = story[currentLine];

            int x = (w - game.textWidth(line, "Georgia", fontSize)) / 2;
            int y = h / 2 + 120;

            game.changeColor(new Color(20, 20, 20));
            game.drawText(x + shadowOff, y + shadowOff, line, "Georgia", fontSize);

            game.changeColor(currentLine >= 5 ? new Color(255, 230, 180) : Color.WHITE);
            game.drawText(x, y, line, "Georgia", fontSize);
        }
        if ((System.currentTimeMillis() / 600) % 2 == 0) {
            game.changeColor(new Color(255, 220, 80));
            game.drawBoldText(w / 2 - 90, h - 40, "Press SPACE to skip", "Arial", 18);
        }
    }
}