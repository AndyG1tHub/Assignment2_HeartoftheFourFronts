/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package game;

/** Stores shared constants so gameplay code avoids magic numbers. */
public final class GameConfig {
    public static final String TITLE = "Heart of the Four Fronts: The Last Defence";

    public static int WINDOW_WIDTH = 900;
    public static int WINDOW_HEIGHT = 700;
    public static final int HUD_WIDTH = 320;
    public static final int TARGET_FPS = 60;

    public static final int GRID_ROWS = 20;
    public static final int GRID_COLS = 20;
    public static int TILE_SIZE = 50;
    public static int MAP_OFFSET_X = 30;
    public static int MAP_OFFSET_Y = 50;

    public static final int BASE_MAX_HP = 1500;
    public static final int STARTING_MONEY = 280;

    public static final int ARROW_TOWER_COST = 50;
    public static final int CANNON_TOWER_COST = 90;
    public static final int ICE_TOWER_COST = 80;
    public static final int LIGHTNING_TOWER_COST = 95;
    public static final int HEAL_TOWER_COST = 70;
    public static final int DECOY_COST = 40;
    public static final double BUILDING_SELL_RATIO = 0.6;
    public static final int MAX_UPGRADE_LEVEL = 2;
    public static final double UPGRADE_COST_MULTIPLIER = 0.8;
    public static final double[] DAMAGE_MULTIPLIER = {1.0, 1.5, 2.0};
    public static final int[] RANGE_BONUS = {0, 1, 1};
    public static final double[] ATTACK_INTERVAL_MULTIPLIER = {1.0, 0.85, 0.7};
    public static final int[] HEAL_AMOUNT_UPGRADE = {25, 40, 60};
    public static final double BOSS_INTERVAL = 40.0;
    public static final int BOSS_BASE_HP = 2000;
    public static final int BOSS_DAMAGE = 50;
    public static final double BOSS_SPEED = 0.95;

    // Boss HP scales with level
    public static int getBossHp(int level) {
        switch (level) {
            case 3: return 1200;  // Mini boss
            case 4: return 1600;  // Standard boss
            case 5: return 2000;  // Ultimate boss
            default: return 2000;
        }
    }

    public static final int ELITE_BASE_HP = 500;
    public static final int ELITE_DAMAGE = 35;
    public static final double ELITE_SPEED = 0.9;

    public static final int REWARD_MONEY = 50;
    public static final int REWARD_SCORE = 100;
    public static final double REWARD_SPAWN_INTERVAL = 4.0;
    public static final double REWARD_SPAWN_CHANCE = 0.8;
    public static final double REWARD_DURATION = 6.0;

    public static final int TOTAL_LEVELS = 5;
    public static final int TOTAL_STAGES = 5;
    public static final double DEFAULT_SPAWN_INTERVAL = 2.0;
    public static final double WAVE_LENGTH_SECONDS = 40.0;

    // LEVEL 1: Only basic melee enemies (player only has arrow tower)
    public static final double LEVEL_ONE_ASSASSIN_CHANCE = 0.0;
    public static final double LEVEL_ONE_ARCHER_CHANCE = 0.0;
    public static final double LEVEL_ONE_TANK_CHANCE = 0.0;

    // LEVEL 2: Introduce fast assassins (player gets ice tower to counter)
    public static final double LEVEL_TWO_TANK_CHANCE = 0.0;
    public static final double LEVEL_TWO_ASSASSIN_CHANCE = 0.30;
    public static final double LEVEL_TWO_ARCHER_CHANCE = 0.10;

    // LEVEL 3: Introduce tanks (player gets cannon tower for AOE)
    public static final double LEVEL_THREE_TANK_CHANCE = 0.25;
    public static final double LEVEL_THREE_ASSASSIN_CHANCE = 0.20;
    public static final double LEVEL_THREE_ARCHER_CHANCE = 0.15;

    // LEVEL 4: More variety (player gets heal tower for sustain)
    public static final double LEVEL_FOUR_TANK_CHANCE = 0.22;
    public static final double LEVEL_FOUR_ASSASSIN_CHANCE = 0.20;
    public static final double LEVEL_FOUR_ARCHER_CHANCE = 0.18;

    // LEVEL 5: All enemy types balanced (player has all towers)
    public static final double LEVEL_FIVE_TANK_CHANCE = 0.20;
    public static final double LEVEL_FIVE_ASSASSIN_CHANCE = 0.20;
    public static final double LEVEL_FIVE_ARCHER_CHANCE = 0.20;

    public static java.util.List<building.BuildingType> getUnlockedTowers(int level) {
        java.util.List<building.BuildingType> list = new java.util.ArrayList<>();
        list.add(building.BuildingType.ARROW_TOWER);
        if (level >= 2) { list.add(building.BuildingType.ICE_TOWER); }
        if (level >= 3) { list.add(building.BuildingType.CANNON_TOWER); }
        if (level >= 4) { list.add(building.BuildingType.HEAL_TOWER); }
        if (level >= 5) { list.add(building.BuildingType.LIGHTNING_TOWER); list.add(building.BuildingType.DECOY); }
        return list;
    }

    private GameConfig() {
    }
}
