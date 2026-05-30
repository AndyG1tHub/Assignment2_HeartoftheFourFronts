/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)


 * Event categories used by EventManager.
 * REWARD_POINT: clickable money/score pickup.
 * FIRE_ZONE: reserved for future use.
 * METEOR_STRIKE: destroys everything in a 3x3 area.
 */
package event;

/** Event categories used by EventManager. */
public enum EventType {
    REWARD_POINT,
    FIRE_ZONE,
    METEOR_STRIKE
}
