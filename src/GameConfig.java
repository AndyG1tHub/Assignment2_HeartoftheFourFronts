/** Stores shared constants so gameplay code avoids magic numbers. */
public final class GameConfig {
    public static final String TITLE = "Heart of the Four Fronts: The Last Defence";

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TARGET_FPS = 60;

    public static final int GRID_ROWS = 20;
    public static final int GRID_COLS = 20;
    public static final int TILE_SIZE = 30;
    public static final int MAP_OFFSET_X = 30;
    public static final int MAP_OFFSET_Y = 50;

    public static final int BASE_MAX_HP = 1000;
    public static final int STARTING_MONEY = 150;
    public static final double BASE_INCOME_PER_SECOND = 2.0;

    public static final int ARROW_TOWER_COST = 40;
    public static final int CANNON_TOWER_COST = 75;
    public static final int ICE_TOWER_COST = 65;
    public static final int LIGHTNING_TOWER_COST = 90;
    public static final int WALL_COST = 20;
    public static final int HEAL_TOWER_COST = 80;
    public static final int DECOY_COST = 35;

    public static final int REWARD_MONEY = 25;
    public static final int REWARD_SCORE = 100;
    public static final double REWARD_SPAWN_INTERVAL = 12.0;

    public static final double DEFAULT_SPAWN_INTERVAL = 2.0;
    public static final double WAVE_LENGTH_SECONDS = 45.0;

    private GameConfig() {
    }
}
