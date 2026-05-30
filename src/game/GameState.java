/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package game;

/**
 * <p>Enumeration of all possible game states: MENU, PLAYING, PAUSED, GAME_OVER, VICTORY, LEVEL_SELECT, HELP, INTRO and others.</p>
 */
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