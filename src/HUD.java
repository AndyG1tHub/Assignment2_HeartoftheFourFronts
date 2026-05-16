import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/** Draws in-game stats and build buttons. */
public class HUD {
    private final List<Button> buttons = new ArrayList<Button>();

    public HUD() {
        createButtons();
    }

    private void createButtons() {
        int x = 660;
        int y = 170;
        buttons.add(new Button(x, y, 190, 28, "1 Arrow $" + GameConfig.ARROW_TOWER_COST, BuildingType.ARROW_TOWER));
        buttons.add(new Button(x, y + 34, 190, 28, "2 Cannon $" + GameConfig.CANNON_TOWER_COST, BuildingType.CANNON_TOWER));
        buttons.add(new Button(x, y + 68, 190, 28, "3 Ice $" + GameConfig.ICE_TOWER_COST, BuildingType.ICE_TOWER));
        buttons.add(new Button(x, y + 102, 190, 28, "4 Lightning $" + GameConfig.LIGHTNING_TOWER_COST, BuildingType.LIGHTNING_TOWER));
        buttons.add(new Button(x, y + 136, 190, 28, "5 Wall $" + GameConfig.WALL_COST, BuildingType.WALL));
        buttons.add(new Button(x, y + 170, 190, 28, "6 Heal $" + GameConfig.HEAL_TOWER_COST, BuildingType.HEAL_TOWER));
        buttons.add(new Button(x, y + 204, 190, 28, "7 Decoy $" + GameConfig.DECOY_COST, BuildingType.DECOY));
    }

    public void draw(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty,
            BuildingType selected, GameState state) {
        drawPanel(engine);
        drawStats(engine, base, economy, score, waves, difficulty, selected);
        drawButtons(engine, selected);
        drawStateOverlay(engine, state);
    }

    private void drawPanel(GameEngine engine) {
        engine.changeColor(new Color(30, 34, 38));
        engine.drawSolidRectangle(640, 0, GameConfig.WINDOW_WIDTH - 640, GameConfig.WINDOW_HEIGHT);
    }

    private void drawStats(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty, BuildingType selected) {
        engine.changeColor(Color.WHITE);
        engine.drawText(660, 40, "Heart of the Four Fronts", "Arial", 18);
        engine.drawText(660, 75, "Base: " + base.getHp() + "/" + base.getMaxHp(), "Arial", 15);
        engine.drawText(660, 100, "Money: $" + economy.getMoney(), "Arial", 15);
        engine.drawText(660, 125, "Score: " + score.getScore(), "Arial", 15);
        engine.drawText(660, 150, "Stage: " + waves.getStage() + "  " + difficulty, "Arial", 15);
        engine.drawText(660, 430, "Selected: " + selected, "Arial", 14);
    }

    private void drawButtons(GameEngine engine, BuildingType selected) {
        for (Button button : buttons) {
            button.draw(engine, button.getBuildingType() == selected);
        }
    }

    private void drawStateOverlay(GameEngine engine, GameState state) {
        if (state == GameState.PAUSED) {
            drawCenteredMessage(engine, "PAUSED");
        } else if (state == GameState.GAME_OVER) {
            drawCenteredMessage(engine, "GAME OVER");
        } else if (state == GameState.WIN) {
            drawCenteredMessage(engine, "YOU WIN");
        }
    }

    private void drawCenteredMessage(GameEngine engine, String text) {
        engine.changeColor(Color.WHITE);
        engine.drawBoldText(250, 340, text, "Arial", 42);
    }

    public BuildingType getClickedBuildingType(int mouseX, int mouseY) {
        for (Button button : buttons) {
            if (button.contains(mouseX, mouseY)) {
                return button.getBuildingType();
            }
        }
        return null;
    }
}
