package game;

/** Stores shared constants so gameplay code avoids magic numbers. */
public final class GameConfig {
    public static final String TITLE = "Heart of the Four Fronts: The Last Defence";

    public static int WINDOW_WIDTH = 900;
    public static int WINDOW_HEIGHT = 700;
    public static final int TARGET_FPS = 60;

    public static final int GRID_ROWS = 20;
    public static final int GRID_COLS = 20;
    public static int TILE_SIZE = 30;
    public static int MAP_OFFSET_X = 30;
    public static int MAP_OFFSET_Y = 50;

    public static final int BASE_MAX_HP = 1200;
    public static final int STARTING_MONEY = 200;
    public static final double BASE_INCOME_PER_SECOND = 2.5;

    public static final int ARROW_TOWER_COST = 55;
    public static final int CANNON_TOWER_COST = 80;
    public static final int ICE_TOWER_COST = 45;
    public static final int LIGHTNING_TOWER_COST = 100;
    public static final int WALL_COST = 35;
    public static final int HEAL_TOWER_COST = 65;
    public static final int DECOY_COST = 40;

    public static final int REWARD_MONEY = 25;
    public static final int REWARD_SCORE = 100;
    public static final double REWARD_SPAWN_INTERVAL = 12.0;

    public static final double DEFAULT_SPAWN_INTERVAL = 2.0;
    public static final double WAVE_LENGTH_SECONDS = 45.0;

    public static final double STAGE_ONE_ASSASSIN_CHANCE = 0.20;
    public static final double STAGE_ONE_ARCHER_CHANCE = 0.15;

    public static final double STAGE_TWO_TANK_CHANCE = 0.20;
    public static final double STAGE_TWO_ASSASSIN_CHANCE = 0.15;
    public static final double STAGE_TWO_ARCHER_CHANCE = 0.15;
    public static final double STAGE_TWO_HEALER_CHANCE = 0.08;

    public static final double STAGE_THREE_TANK_CHANCE = 0.18;
    public static final double STAGE_THREE_ASSASSIN_CHANCE = 0.16;
    public static final double STAGE_THREE_ARCHER_CHANCE = 0.16;
    public static final double STAGE_THREE_HEALER_CHANCE = 0.12;

    private GameConfig() {
    }
}
