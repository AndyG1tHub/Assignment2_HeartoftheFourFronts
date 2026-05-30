/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package game;

/** Represents the top-level screen or play state of the game. */
public enum GameState {

    INTRO,
    MENU,
    PLAYING,
    PAUSED,
    GAME_OVER_EFFECT,
    WIN_EFFECT,
    GAME_OVER,
    WIN
}