/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Abstract base for all timed map events with a common lifecycle.
 * Holds a type enum, a countdown duration, and an active flag.
 * Subclasses override draw() for rendering and add effect logic.
 * Duration ticks down each frame; when zero, marked finished and removed.
 */
package event;

import core.GridMap;
import game.GameEngine;

/** Abstract timed event with a common update/draw lifecycle. */
public abstract class GameEvent {
    protected final EventType type;
    protected double duration;
    protected boolean active;

    protected GameEvent(EventType type, double duration) {
        this.type = type;
        this.duration = duration;
        this.active = true;
    }

    public void update(double dt) {
        duration -= dt;
        if (duration <= 0.0) {
            active = false;
        }
    }

    public abstract void draw(GameEngine engine, GridMap map);

    public boolean isFinished() {
        return !active;
    }

    public EventType getType() {
        return type;
    }
}
