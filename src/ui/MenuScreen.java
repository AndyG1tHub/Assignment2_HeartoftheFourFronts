package ui;

import java.awt.Color;
import java.awt.Image;

import game.GameEngine;
import game.GameConfig;
import game.Difficulty;
import manager.ImageManger;

/** Main menu screen — premium minimal UI that showcases the background art. */
public class MenuScreen {
    private static final Color BG = new Color(20, 24, 28);
    private static final Color GOLD = new Color(235, 200, 110);
    private static final Color GOLD_DIM = new Color(170, 140, 60);
    private static final Color MUTED = new Color(185, 190, 195);
    private static final Color LOCKED = new Color(90, 55, 55);

    public static final int END_NONE = 0;
    public static final int END_RESTART = 1;
    public static final int END_MENU = 2;
    public static final int CONTINUE = 3;
    public static final int LEVEL_PLAY = 10;

    private int menuState;
    private boolean hasContinue;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private int maxUnlockedLevel = 1;
    private int pendingLevel;

    // Buttons constructed with actual rendered size — setSize/setPosition used each frame
    private final Button continueButton = new Button(0, 0, 240, 42, "CONTINUE", null, new Color(100, 215, 165));
    private final Button playButton      = new Button(0, 0, 240, 42, "PLAY",     null, new Color(100, 205, 135));
    private final Button diffButton      = new Button(0, 0, 240, 42, "",         null, new Color(235, 200, 110));
    private final Button helpButton      = new Button(0, 0, 240, 42, "HELP",     null, new Color(145, 165, 195));
    private final Button exitButton      = new Button(0, 0, 240, 42, "EXIT",     null, new Color(205, 120, 120));
    private final Button backButton      = new Button(0, 0, 200, 42, "BACK",     null, new Color(185, 190, 195));
    private final Button restartButton   = new Button(0, 0, 260, 44, "RESTART",  null, new Color(235, 200, 110));
    private final Button menuButton      = new Button(0, 0, 260, 44, "MAIN MENU",null, new Color(185, 190, 195));
    private final Button[] levelButtons  = new Button[5];

    public MenuScreen() {
        menuState = 0;
        updateDiffLabel();
        for (int i = 0; i < 5; i++) {
            levelButtons[i] = new Button(0, 0, 200, 56, "Level " + (i + 1), null, new Color(100, 205, 135));
        }
    }

    public void setMaxUnlockedLevel(int level) { maxUnlockedLevel = Math.max(1, Math.min(level, GameConfig.TOTAL_LEVELS)); }
    public int getSelectedLevel() { return pendingLevel; }
    public void setHasContinue(boolean exists) { hasContinue = exists; }
    private void updateDiffLabel() { diffButton.setText("" + currentDifficulty); }
    //                                                      ^ just the name, short

    // ================================================================
    //  TOP-LEVEL DRAW
    // ================================================================

    public void draw(GameEngine engine, int mouseX, int mouseY) {
        // 1. Background image — full screen, no global overlay
        Image bg = ImageManger.getBackground();
        if (bg != null) {
            engine.drawImage(bg, 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        } else {
            engine.changeColor(BG);
            engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        }

        // 2. Subtle bottom gradient — only darkens the lower portion so buttons are readable
        drawBottomVignette(engine);

        // 3. Route to correct screen
        if (menuState == 1) {
            drawHelpContent(engine, mouseX, mouseY);
        } else if (menuState == 2) {
            drawLevelSelect(engine, mouseX, mouseY);
        } else {
            drawMainMenu(engine, mouseX, mouseY);
        }
    }

    /** Draw 8 gradient bands at bottom — transparency increases toward bottom. */
    private void drawBottomVignette(GameEngine engine) {
        int h = GameConfig.WINDOW_HEIGHT;
        int w = GameConfig.WINDOW_WIDTH;
        int bands = 8;
        int bandH = 28;
        for (int i = 0; i < bands; i++) {
            int alpha = 12 + i * 16;               // 12 → 124
            engine.changeColor(new Color(4, 6, 10, alpha));
            engine.drawSolidRectangle(0, h - (bands - i) * bandH, w, bandH);
        }
    }

    // ================================================================
    //  MAIN MENU  —  right-side vertical button column, background shines through
    // ================================================================

    private void drawMainMenu(GameEngine engine, int mouseX, int mouseY) {
        int w = GameConfig.WINDOW_WIDTH;
        int h = GameConfig.WINDOW_HEIGHT;

        int btnW = 380;
        int btnH = 62;
        int gap = 14;
        int count = hasContinue ? 5 : 4;
        int totalH = count * btnH + (count - 1) * gap;
        int startY = (h - totalH) / 2 + 150;
        int colX = (w - btnW) / 2;

        int idx = 0;
        if (hasContinue) {
            drawMenuBtn(engine, colX, startY + idx * (btnH + gap), btnW, btnH,
                    "CONTINUE", new Color(100, 215, 165), mouseX, mouseY, continueButton);
            idx++;
        }
        drawMenuBtn(engine, colX, startY + idx * (btnH + gap), btnW, btnH,
                "PLAY", new Color(100, 205, 135), mouseX, mouseY, playButton);
        idx++;
        drawMenuBtn(engine, colX, startY + idx * (btnH + gap), btnW, btnH,
                diffButton.getText(), new Color(235, 200, 110), mouseX, mouseY, diffButton);
        idx++;
        drawMenuBtn(engine, colX, startY + idx * (btnH + gap), btnW, btnH,
                "HELP", new Color(145, 165, 195), mouseX, mouseY, helpButton);
        idx++;
        drawMenuBtn(engine, colX, startY + idx * (btnH + gap), btnW, btnH,
                "EXIT", new Color(205, 120, 120), mouseX, mouseY, exitButton);

        engine.changeColor(new Color(140, 145, 150, 90));
        engine.drawText(w / 2 - 100, h - 32, "Space to skip  |  Esc to return", "Arial", 11);
    }

    // ================================================================
    //  HELP SCREEN  —  centred panel, moderate overlay
    // ================================================================

    private void drawHelpContent(GameEngine engine, int mouseX, int mouseY) {
        drawScreenOverlay(engine, 185);

        int panelW = 800;
        int panelH = 600;
        int px = (GameConfig.WINDOW_WIDTH - panelW) / 2;
        int py = (GameConfig.WINDOW_HEIGHT - panelH) / 2;

        drawGlassPanel(engine, px, py, panelW, panelH);
        drawPanelHeading(engine, px + 50, py + 36, "HOW  TO  PLAY");

        int x = px + 60;
        int y = py + 74;
        int s = 22;

        drawHelpSection(engine, x, y, s, "CONTROLS", new String[]{
            "1-7  Select building type        Space  Pause / Resume",
            "Left Click  Place building       Esc    Return to menu",
            "Right Click  Sell (60% refund)   F      Toggle 2x speed",
            "Click button  Select from panel  M      Toggle sound",
        });
        y += 4 * s + 12;

        drawSectionDivider(engine, x, y, x + 680);
        y += 14;

        drawHelpSection(engine, x, y, s, "PAUSE MENU", new String[]{
            "Resume  Continue playing        Delete Save  Remove data",
            "Save    Save progress            Restart  Restart level",
            "Main Menu  Return to title screen",
        });
        y += 3 * s + 12;

        drawSectionDivider(engine, x, y, x + 680);
        y += 14;

        drawHelpSection(engine, x, y, s, "TIPS", new String[]{
            "Hover building buttons to preview their stats",
            "Resize window - map scales to fit",
            "Wall limit: Easy=unlimited, Normal=16, Hard=8",
            "5s prep time before each wave",
            "Win: defeat final boss",
            "Save deletes on win or game over",
        });

        backButton.setSize(280, 50);
        backButton.setPosition(px + (panelW - 280) / 2, py + panelH - 70);
        drawBackBtn(engine, backButton, mouseX, mouseY);
    }

    private void drawSectionDivider(GameEngine engine, int x1, int y, int x2) {
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 50));
        engine.drawLine(x1, y, x2, y);
    }

    private void drawHelpSection(GameEngine engine, int x, int y, int s, String hdr, String[] lines) {
        engine.changeColor(GOLD);
        engine.drawBoldText(x, y, hdr, "Arial", 18);
        y += s;
        engine.changeColor(MUTED);
        for (String line : lines) {
            engine.drawText(x, y, line, "Arial", 16);
            y += s;
        }
    }

    // ================================================================
    //  LEVEL SELECT
    // ================================================================

    private void drawLevelSelect(GameEngine engine, int mouseX, int mouseY) {
        drawScreenOverlay(engine, 175);

        int panelW = 720;
        int panelH = 500;
        int px = (GameConfig.WINDOW_WIDTH - panelW) / 2;
        int py = (GameConfig.WINDOW_HEIGHT - panelH) / 2;

        drawGlassPanel(engine, px, py, panelW, panelH);
        drawPanelHeading(engine, px + 50, py + 36, "SELECT  LEVEL");

        engine.changeColor(MUTED);
        engine.drawText(px + 210, py + 70, "Complete a level to unlock the next", "Arial", 16);

        int sx = px + 70;
        int sy = py + 100;
        for (int i = 0; i < 5; i++) {
            int lvl = i + 1;
            boolean unlocked = lvl <= maxUnlockedLevel;
            Button btn = levelButtons[i];
            int bx = sx + (i % 2) * 300;
            int by = sy + (i / 2) * 100;
            btn.setSize(280, 74);
            btn.setPosition(bx, by);
            boolean hov = btn.contains(mouseX, mouseY);

            if (unlocked) {
                drawLevelBtn(engine, bx, by, 280, 74, "Level " + lvl,
                        new Color(100, 205, 135), hov);
            } else {
                engine.changeColor(new Color(28, 20, 20, 210));
                engine.drawSolidRectangle(bx, by, 280, 74);
                engine.changeColor(new Color(50, 30, 30));
                engine.drawRectangle(bx, by, 280, 74);
                engine.changeColor(LOCKED);
                engine.drawBoldText(bx + 80, by + 46, "Level " + lvl + "  ?", "Arial", 18);
            }
        }

        backButton.setSize(280, 52);
        backButton.setPosition(px + (panelW - 280) / 2, py + panelH - 70);
        drawBackBtn(engine, backButton, mouseX, mouseY);
    }

    // ================================================================
    //  END SCREEN
    // ================================================================

    public void drawEndScreen(GameEngine engine, boolean won, int mouseX, int mouseY) {
        int w = GameConfig.WINDOW_WIDTH;
        int h = GameConfig.WINDOW_HEIGHT;

        Image bg = ImageManger.getBackground();
        if (bg != null) {
            engine.drawImage(bg, 0, 0, w, h);
        } else {
            engine.changeColor(BG);
            engine.drawSolidRectangle(0, 0, w, h);
        }
        drawScreenOverlay(engine, 140);
        drawBottomVignette(engine);

        String title = won ? "VICTORY" : "DEFEAT";
        Color tc = won ? new Color(100, 230, 145) : new Color(240, 90, 80);

        int tw = engine.textWidth(title, "Georgia", 52);
        int tx = (w - tw) / 2;
        int ty = h / 2 + 50;
        engine.changeColor(new Color(0, 0, 0, 110));
        engine.drawBoldText(tx + 3, ty + 3, title, "Georgia", 52);
        engine.changeColor(tc);
        engine.drawBoldText(tx, ty, title, "Georgia", 52);

        engine.changeColor(MUTED);
        int stw = engine.textWidth("Choose your next action", "Arial", 16);
        engine.drawText((w - stw) / 2, ty + 36, "Choose your next action", "Arial", 16);

        int btnW = 380;
        int btnH = 62;
        int gap = 14;
        int colX = (w - btnW) / 2;
        int startY = ty + 80;

        restartButton.setSize(btnW, btnH);
        restartButton.setPosition(colX, startY);
        drawMenuBtn(engine, colX, startY, btnW, btnH,
                "RESTART", new Color(235, 200, 110), mouseX, mouseY, restartButton);

        menuButton.setSize(btnW, btnH);
        menuButton.setPosition(colX, startY + btnH + gap);
        drawMenuBtn(engine, colX, startY + btnH + gap, btnW, btnH,
                "MAIN MENU", new Color(185, 190, 195), mouseX, mouseY, menuButton);
    }

    // ================================================================
    //  DRAWING HELPERS
    // ================================================================

    private void drawScreenOverlay(GameEngine engine, int alpha) {
        engine.changeColor(new Color(6, 8, 12, alpha));
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    }

    /** Glassmorphism panel — dark semi-transparent with thin border + corner accents. */
    private void drawGlassPanel(GameEngine engine, int x, int y, int w, int h) {
        engine.changeColor(new Color(10, 13, 19, 215));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(new Color(55, 60, 66, 130));
        engine.drawRectangle(x, y, w, h);

        // Corner accents
        int c = 22;
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 110));
        engine.drawLine(x, y, x + c, y);   engine.drawLine(x, y, x, y + c);
        engine.drawLine(x + w, y, x + w - c, y);   engine.drawLine(x + w, y, x + w, y + c);
        engine.drawLine(x, y + h, x + c, y + h);   engine.drawLine(x, y + h, x, y + h - c);
        engine.drawLine(x + w, y + h, x + w - c, y + h);   engine.drawLine(x + w, y + h, x + w, y + h - c);
    }

    private void drawPanelHeading(GameEngine engine, int x, int y, String title) {
        engine.changeColor(new Color(0, 0, 0, 110));
        engine.drawBoldText(x + 3, y + 3, title, "Georgia", 28);
        engine.changeColor(GOLD);
        engine.drawBoldText(x, y, title, "Georgia", 28);
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 60));
        engine.drawLine(x, y + 14, x + 340, y + 14);
    }

    // ================================================================
    //  BUTTON STYLES
    // ================================================================

    /** Main-menu right-column button — low-alpha glass feel. */
    private void drawMenuBtn(GameEngine engine, int x, int y, int w, int h,
            String text, Color accent, int mouseX, int mouseY, Button btn) {
        btn.setSize(w, h);
        btn.setPosition(x, y);
        boolean on = btn.contains(mouseX, mouseY);

        // fill
        engine.changeColor(on
                ? new Color(30, 36, 46, 190)
                : new Color(14, 18, 24, 120));
        engine.drawSolidRectangle(x, y, w, h);

        // left accent bar
        engine.changeColor(on ? accent : new Color(accent.getRed()/3, accent.getGreen()/3, accent.getBlue()/3, 160));
        engine.drawSolidRectangle(x, y, 4, h);

        // border
        engine.changeColor(on ? accent : new Color(60, 65, 72, 110));
        engine.drawRectangle(x, y, w, h);

        // text
        engine.changeColor(on ? Color.WHITE : new Color(210, 215, 220));
        int fs = text.length() > 10 ? 20 : 24;
        int tw = text.length() * fs * 6 / 10;
        engine.drawBoldText(x + (w - tw) / 2, y + 42, text, "Arial", fs);
    }

    private void drawBackBtn(GameEngine engine, Button btn, int mouseX, int mouseY) {
        boolean on = btn.contains(mouseX, mouseY);
        int x = btn.getX(), y = btn.getY(), w = btn.getWidth(), h = btn.getHeight();

        engine.changeColor(on ? new Color(32, 38, 48, 210) : new Color(16, 20, 27, 160));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(on ? GOLD : new Color(60, 65, 72, 130));
        engine.drawRectangle(x, y, w, h);
        engine.changeColor(on ? Color.WHITE : MUTED);
        engine.drawBoldText(x + (w - 56) / 2, y + 34, "BACK", "Arial", 17);
    }

    private void drawEndBtn(GameEngine engine, Button btn, int mouseX, int mouseY) {
        boolean on = btn.contains(mouseX, mouseY);
        int x = btn.getX(), y = btn.getY(), w = btn.getWidth(), h = btn.getHeight();

        engine.changeColor(on ? new Color(38, 44, 56, 210) : new Color(18, 22, 30, 160));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(on ? GOLD : new Color(60, 65, 72, 130));
        engine.drawRectangle(x, y, w, h);
        engine.changeColor(on ? Color.WHITE : MUTED);
        String t = btn.getText();
        int tw = t.length() * 9;
        engine.drawBoldText(x + (w - tw) / 2, y + 30, t, "Arial", 14);
    }

    private void drawLevelBtn(GameEngine engine, int x, int y, int w, int h,
            String text, Color accent, boolean on) {
        engine.changeColor(on ? new Color(34, 60, 42, 210) : new Color(14, 22, 18, 160));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(on ? accent : new Color(accent.getRed()/3, accent.getGreen()/3, accent.getBlue()/3, 160));
        engine.drawSolidRectangle(x, y, 5, h);
        engine.changeColor(on ? accent : new Color(50, 60, 55, 130));
        engine.drawRectangle(x, y, w, h);
        engine.changeColor(on ? Color.WHITE : MUTED);
        int tw = text.length() * 13;
        engine.drawBoldText(x + (w - tw) / 2, y + 47, text, "Arial", 20);
    }

    // ================================================================
    //  CLICK HANDLING
    // ================================================================

    public int handleClick(int mouseX, int mouseY) {
        if (menuState == 1) {
            if (backButton.contains(mouseX, mouseY)) { menuState = 0; return MenuAction.SOUND; }
            return MenuAction.NONE;
        }
        if (menuState == 2) {
            if (backButton.contains(mouseX, mouseY)) { menuState = 0; return MenuAction.SOUND; }
            for (int i = 0; i < 5; i++) {
                if (i + 1 <= maxUnlockedLevel && levelButtons[i].contains(mouseX, mouseY)) {
                    pendingLevel = i + 1;
                    menuState = 0;
                    return LEVEL_PLAY;
                }
            }
            return MenuAction.NONE;
        }
        if (hasContinue && continueButton.contains(mouseX, mouseY)) return CONTINUE;
        if (playButton.contains(mouseX, mouseY))      { menuState = 2; return MenuAction.SOUND; }
        if (diffButton.contains(mouseX, mouseY))      { cycleDifficulty(); return MenuAction.SOUND; }
        if (helpButton.contains(mouseX, mouseY))      { menuState = 1; return MenuAction.SOUND; }
        if (exitButton.contains(mouseX, mouseY))      { System.exit(0); }
        return MenuAction.NONE;
    }

    private void cycleDifficulty() {
        if (currentDifficulty == Difficulty.EASY) currentDifficulty = Difficulty.NORMAL;
        else if (currentDifficulty == Difficulty.NORMAL) currentDifficulty = Difficulty.HARD;
        else currentDifficulty = Difficulty.EASY;
        updateDiffLabel();
    }

    public Difficulty getSelectedDifficulty() { return currentDifficulty; }

    public int handleEndClick(int mouseX, int mouseY) {
        if (restartButton.contains(mouseX, mouseY)) return END_RESTART;
        if (menuButton.contains(mouseX, mouseY))    return END_MENU;
        return END_NONE;
    }

    public static class MenuAction {
        public static final int NONE = 0;
        public static final int PLAY = 1;
        public static final int SOUND = 2;
    }
}
