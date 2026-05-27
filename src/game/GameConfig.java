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
    public static int getMaxWalls(Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) return Integer.MAX_VALUE;
        if (difficulty == Difficulty.NORMAL) return 16;
        return 8;
    }
    public static final double BUILDING_SELL_RATIO = 0.6;
    public static final int MAX_UPGRADE_LEVEL = 2;
    public static final double UPGRADE_COST_MULTIPLIER = 0.8;
    public static final double[] DAMAGE_MULTIPLIER = {1.0, 1.5, 2.0};
    public static final int[] RANGE_BONUS = {0, 1, 1};
    public static final double[] ATTACK_INTERVAL_MULTIPLIER = {1.0, 0.85, 0.7};
    public static final int[] HEAL_AMOUNT_UPGRADE = {20, 30, 45};
    public static final double BOSS_INTERVAL = 40.0;
    public static final int BOSS_BASE_HP = 600;
    public static final int BOSS_DAMAGE = 60;
    public static final double BOSS_SPEED = 0.85;

    public static final int REWARD_MONEY = 40;
    public static final int REWARD_SCORE = 100;
    public static final double REWARD_SPAWN_INTERVAL = 12.0;

    public static final int TOTAL_LEVELS = 5;
    public static final int TOTAL_STAGES = 5;
    public static final double DEFAULT_SPAWN_INTERVAL = 2.0;
    public static final double WAVE_LENGTH_SECONDS = 40.0;

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

    public static final double STAGE_FOUR_TANK_CHANCE = 0.18;
    public static final double STAGE_FOUR_ASSASSIN_CHANCE = 0.18;
    public static final double STAGE_FOUR_ARCHER_CHANCE = 0.18;
    public static final double STAGE_FOUR_HEALER_CHANCE = 0.15;

    public static final double STAGE_FIVE_TANK_CHANCE = 0.20;
    public static final double STAGE_FIVE_ASSASSIN_CHANCE = 0.20;
    public static final double STAGE_FIVE_ARCHER_CHANCE = 0.20;
    public static final double STAGE_FIVE_HEALER_CHANCE = 0.18;

    public static java.util.List<building.BuildingType> getUnlockedTowers(int level) {
        java.util.List<building.BuildingType> list = new java.util.ArrayList<>();
        list.add(building.BuildingType.ARROW_TOWER);
        if (level >= 2) { list.add(building.BuildingType.WALL); list.add(building.BuildingType.CANNON_TOWER); }
        if (level >= 3) { list.add(building.BuildingType.ICE_TOWER); list.add(building.BuildingType.HEAL_TOWER); }
        if (level >= 4) { list.add(building.BuildingType.LIGHTNING_TOWER); }
        if (level >= 5) { list.add(building.BuildingType.DECOY); }
        return list;
    }

    private GameConfig() {
    }
}
