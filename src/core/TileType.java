/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Logical state labels for grid tiles.
 * EMPTY is unoccupied and buildable; OBSTACLE is a permanent blocker.
 * BASE is the fortress centre; BUILDING holds a player tower.
 * DISASTER marks tiles affected by a natural disaster overlay.
 */
package core;

/** Logical state of one grid tile. */
public enum TileType {
    EMPTY,
    OBSTACLE,
    BASE,
    BUILDING,
    DISASTER
}
