/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Four cardinal directions for decoy movement and edge spawning.
 * NORTH is up (-row), SOUTH is down (+row).
 * WEST is left (-col), EAST is right (+col).
 * Decoy launch direction is chosen by the click position relative to base.
 */
package util;

/** Four cardinal directions used for spawning and decoy movement. */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}
