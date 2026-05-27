package ui;

import java.awt.Color;
import java.awt.Image;

import game.GameEngine;
import game.GameConfig;
import game.Difficulty;
import manager.ImageManger;

/** Main menu screen with Play, Difficulty, Help, Exit options. */
public class MenuScreen {
    private static final Color BG = new Color(20, 24, 28);
    private static final Color ACCENT = new Color(200, 180, 120);
    private static final Color SUBTITLE = new Color(160, 165, 170);

    public static final int END_NONE = 0;
    public static final int END_RESTART = 1;
    public static final int END_MENU = 2;
    public static final int CONTINUE = 3;

    private int menuState;
    private boolean hasContinue;
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private final Button continueButton = new Button(320, 270, 200, 34, "CONTINUE", null, new Color(80, 200, 160));
    private final Button playButton = new Button(320, 312, 200, 34, "PLAY", null, new Color(80, 180, 120));
    private final Button diffButton = new Button(320, 354, 200, 34, "", null, new Color(200, 180, 80));
    private final Button helpButton = new Button(320, 396, 200, 34, "HELP", null, new Color(130, 150, 180));
    private final Button exitButton = new Button(320, 438, 200, 34, "EXIT", null, new Color(180, 100, 100));
    private final Button backButton = new Button(320, 420, 200, 34, "BACK", null, SUBTITLE);
    private final Button restartButton = new Button(330, 320, 240, 34, "RESTART", null, new Color(200, 180, 80));
    private final Button menuButton = new Button(330, 365, 240, 34, "MAIN MENU", null, SUBTITLE);

    public MenuScreen() {
        menuState = 0;
        updateDiffLabel();
    }

    public void setHasContinue(boolean exists) {
        hasContinue = exists;
    }

    private void updateDiffLabel() {
        diffButton.setText("DIFFICULTY: " + currentDifficulty);
    }

    public void draw(GameEngine engine, int mouseX, int mouseY) {
        engine.changeColor(BG);
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        drawBorder(engine);
        drawCastle(engine);
        drawTitle(engine);
        drawSubtitle(engine);

        if (menuState == 1) {
            drawHelpContent(engine, mouseX, mouseY);
        } else {
            drawMainButtons(engine, mouseX, mouseY);
        }
        drawFooter(engine);
    }

    private int cx() {
        return GameConfig.WINDOW_WIDTH / 2;
    }

    private void drawCastle(GameEngine engine) {
        int cx = cx() + 210, cy = 70;
        engine.changeColor(new Color(55, 60, 70));
        engine.drawSolidRectangle(cx - 40, cy, 80, 50);
        engine.drawSolidRectangle(cx - 15, cy - 20, 30, 20);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawRectangle(cx - 40, cy, 80, 50);
        engine.drawRectangle(cx - 15, cy - 20, 30, 20);
        for (int i = 0; i < 4; i++) {
            engine.drawSolidRectangle(cx - 35 + i * 20, cy - 6, 10, 6);
        }
        engine.changeColor(new Color(40, 35, 25));
        engine.drawSolidRectangle(cx - 8, cy + 25, 16, 25);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawRectangle(cx - 8, cy + 25, 16, 25);
        engine.drawCircle(cx, cy + 38, 3);
        engine.drawLine(cx, cy + 30, cx, cy + 45);
        engine.drawLine(cx - 8, cy + 38, cx + 8, cy + 38);
    }

    private void drawBorder(GameEngine engine) {
        engine.changeColor(ACCENT);
        engine.drawRectangle(40, 40, GameConfig.WINDOW_WIDTH - 80, GameConfig.WINDOW_HEIGHT - 80, 2);
        engine.drawRectangle(44, 44, GameConfig.WINDOW_WIDTH - 88, GameConfig.WINDOW_HEIGHT - 88, 1);
    }

    private void drawTitle(GameEngine engine) {
        engine.changeColor(ACCENT);
        engine.drawBoldText(cx() - 270, 140, "HEART OF THE", "Arial", 26);
        engine.drawBoldText(cx() - 210, 175, "FOUR FRONTS", "Arial", 36);
        engine.drawLine(cx() - 210, 195, cx() + 130, 195);
    }

    private void drawSubtitle(GameEngine engine) {
        engine.changeColor(SUBTITLE);
        engine.drawText(cx() - 130, 235, "The Last Defence", "Arial", 18);
    }

    private void drawMainButtons(GameEngine engine, int mouseX, int mouseY) {
        int bx = cx() - 100;
        int by = 280;
        if (hasContinue) {
            continueButton.setPosition(bx, by); by += 38;
            drawPlainButton(engine, continueButton, mouseX, mouseY);
        }
        playButton.setPosition(bx, by); by += 38;
        drawPlayButton(engine, playButton, mouseX, mouseY);
        diffButton.setPosition(bx, by); by += 38;
        drawPlainButton(engine, diffButton, mouseX, mouseY);
        helpButton.setPosition(bx, by); by += 38;
        drawPlainButton(engine, helpButton, mouseX, mouseY);
        exitButton.setPosition(bx, by); by += 38;
        drawExitButton(engine, exitButton, mouseX, mouseY);
    }

    private void drawPlayButton(GameEngine engine, Button btn, int mouseX, int mouseY) {
        boolean hovered = btn.contains(mouseX, mouseY);
        engine.drawImage(hovered ? ImageManger.getBtnPlayHover() : ImageManger.getBtnPlayNormal(),
                btn.getX() + 20, btn.getY() - 20, 160, 74);
    }

    private void drawExitButton(GameEngine engine, Button btn, int mouseX, int mouseY) {
        boolean hovered = btn.contains(mouseX, mouseY);
        engine.drawImage(hovered ? ImageManger.getBtnExitHover() : ImageManger.getBtnExitNormal(),
                btn.getX() + 20, btn.getY() - 20, 160, 74);
    }

    private void drawPlainButton(GameEngine engine, Button btn, int mouseX, int mouseY) {
        boolean hovered = btn.contains(mouseX, mouseY);
        Image img = hovered ? ImageManger.getBtnPlainHover() : ImageManger.getBtnPlainNormal();
        int iw = 170, ih = 60;
        int ix = btn.getX() + (btn.getWidth() - iw) / 2;
        int iy = btn.getY() + (btn.getHeight() - ih) / 2;
        engine.drawImage(img, ix, iy, iw, ih);
        engine.changeColor(ACCENT);
        String t = btn.getText();
        int size = t.length() > 14 ? 13 : 15;
        int tw = (int)(t.length() * size * 0.6);
        engine.drawBoldText(ix + (iw - tw) / 2, iy + 39, t, "Arial", size);
    }

    private void drawHelpContent(GameEngine engine, int mouseX, int mouseY) {
        engine.changeColor(Color.WHITE);
        engine.drawBoldText(cx() - 60, 220, "HOW TO PLAY", "Arial", 22);
        engine.changeColor(SUBTITLE);
        int x = cx() - 170, y = 250, step = 18;
        engine.drawText(x, y, "1-7          : Select building type", "Arial", 14); y += step;
        engine.drawText(x, y, "Click map : Place selected building", "Arial", 14); y += step;
        engine.drawText(x, y, "Right-click : Sell tower (60% refund)", "Arial", 14); y += step;
        engine.drawText(x, y, "Click btn   : Select building from panel", "Arial", 14); y += step;
        engine.drawText(x, y, "Space       : Pause / Resume game", "Arial", 14); y += step;
        engine.drawText(x, y, "Esc          : Return to main menu", "Arial", 14); y += step;
        engine.drawText(x, y, "F              : Toggle 2x speed", "Arial", 14); y += step;
        engine.drawText(x, y, "M             : Toggle sound mute", "Arial", 14); y += step;
        y += 10;
        engine.changeColor(new Color(200, 180, 120));
        engine.drawBoldText(x, y, "PAUSE MENU", "Arial", 15); y += step;
        engine.changeColor(SUBTITLE);
        engine.drawText(x, y, "Resume      : Continue playing", "Arial", 14); y += step;
        engine.drawText(x, y, "Save          : Save current progress", "Arial", 14); y += step;
        engine.drawText(x, y, "Delete Save : Remove saved data", "Arial", 14); y += step;
        engine.drawText(x, y, "Restart      : Restart the game", "Arial", 14); y += step;
        engine.drawText(x, y, "Main Menu : Go back to title screen", "Arial", 14); y += step;
        y += 10;
        engine.changeColor(new Color(200, 180, 120));
        engine.drawBoldText(x, y, "TIPS", "Arial", 15); y += step;
        engine.changeColor(SUBTITLE);
        engine.drawText(x, y, "Hover building buttons to see stats", "Arial", 14); y += step;
        engine.drawText(x, y, "Resize window - map auto-adjusts", "Arial", 14); y += step;
        engine.drawText(x, y, "Intro cutscene - press Space to skip", "Arial", 14); y += step;
        engine.drawText(x, y, "Wall limit: Easy=unlimited, N=16, H=8", "Arial", 14); y += step;
        engine.drawText(x, y, "5s prep time before each wave", "Arial", 14); y += step;
        engine.drawText(x, y, "Win: survive 5 waves (200s)", "Arial", 14); y += step;
        engine.drawText(x, y, "Save deletes on win or game over", "Arial", 14); y += step;
        backButton.setPosition(cx() - 100, GameConfig.WINDOW_HEIGHT - 100);
        backButton.draw(engine, false, mouseX, mouseY);
    }

    private void drawFooter(GameEngine engine) {
        engine.changeColor(new Color(100, 105, 110));
        engine.drawText(cx() - 130, GameConfig.WINDOW_HEIGHT - 30, "Defend the heart from four fronts.", "Arial", 13);
    }

    public int handleClick(int mouseX, int mouseY) {
        if (menuState == 1) {
            if (backButton.contains(mouseX, mouseY)) {
                menuState = 0;
                return MenuAction.SOUND;
            }
            return MenuAction.NONE;
        }
        if (hasContinue && continueButton.contains(mouseX, mouseY)) {
            return CONTINUE;
        }
        if (playButton.contains(mouseX, mouseY)) {
            return MenuAction.PLAY;
        }
        if (diffButton.contains(mouseX, mouseY)) {
            cycleDifficulty();
            return MenuAction.SOUND;
        }
        if (helpButton.contains(mouseX, mouseY)) {
            menuState = 1;
            return MenuAction.SOUND;
        }
        if (exitButton.contains(mouseX, mouseY)) {
            System.exit(0);
        }
        return MenuAction.NONE;
    }

    private void cycleDifficulty() {
        if (currentDifficulty == Difficulty.EASY) {
            currentDifficulty = Difficulty.NORMAL;
        } else if (currentDifficulty == Difficulty.NORMAL) {
            currentDifficulty = Difficulty.HARD;
        } else {
            currentDifficulty = Difficulty.EASY;
        }
        updateDiffLabel();
    }

    public Difficulty getSelectedDifficulty() {
        return currentDifficulty;
    }

    public void drawEndScreen(GameEngine engine, boolean won, int mouseX, int mouseY) {
        engine.changeColor(BG);
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        drawBorder(engine);
        engine.changeColor(won ? new Color(80, 200, 120) : new Color(200, 60, 60));
        engine.drawBoldText(cx() - 90, 210, won ? "YOU WIN!" : "GAME OVER", "Arial", 42);
        engine.changeColor(SUBTITLE);
        engine.drawText(cx() - 80, 260, "Choose what to do next.", "Arial", 16);
        restartButton.setPosition(cx() - 120, 320);
        menuButton.setPosition(cx() - 120, 365);
        restartButton.draw(engine, true, mouseX, mouseY);
        menuButton.draw(engine, false, mouseX, mouseY);
    }

    public int handleEndClick(int mouseX, int mouseY) {
        if (restartButton.contains(mouseX, mouseY)) {
            return END_RESTART;
        }
        if (menuButton.contains(mouseX, mouseY)) {
            return END_MENU;
        }
        return END_NONE;
    }

    /** Return codes for menu clicks. */
    public static class MenuAction {
        public static final int NONE = 0;
        public static final int PLAY = 1;
        public static final int SOUND = 2;
    }
}
