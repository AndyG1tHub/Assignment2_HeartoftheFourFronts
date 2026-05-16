import java.awt.Color;

/** Simple menu and difficulty selection screen. */
public class MenuScreen {
    private final Button easyButton = new Button(330, 275, 240, 36, "Start Easy", null);
    private final Button normalButton = new Button(330, 320, 240, 36, "Start Normal", null);
    private final Button hardButton = new Button(330, 365, 240, 36, "Start Hard", null);

    public void draw(GameEngine engine) {
        engine.changeColor(new Color(24, 28, 32));
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(Color.WHITE);
        engine.drawBoldText(125, 170, GameConfig.TITLE, "Arial", 28);
        engine.drawText(280, 225, "Defend the central base from four fronts.", "Arial", 16);
        easyButton.draw(engine, false);
        normalButton.draw(engine, true);
        hardButton.draw(engine, false);
        engine.drawText(285, 455, "Keys: 1-7 build, Space pause, Esc menu", "Arial", 15);
    }

    public Difficulty handleClick(int mouseX, int mouseY) {
        if (easyButton.contains(mouseX, mouseY)) {
            return Difficulty.EASY;
        }
        if (normalButton.contains(mouseX, mouseY)) {
            return Difficulty.NORMAL;
        }
        if (hardButton.contains(mouseX, mouseY)) {
            return Difficulty.HARD;
        }
        return null;
    }
}
