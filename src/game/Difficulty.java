/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Player-selected difficulty tier for the game.
 * EASY: reduced enemy HP, slower spawns, fewer disasters.
 * NORMAL: baseline with slight multipliers for balance.
 * HARD: tougher enemies, faster spawns, shorter disaster intervals.
 */
package game;

/** Player-selected difficulty. DifficultyManager converts this into numbers. */
public enum Difficulty {
    EASY,
    NORMAL,
    HARD
}
