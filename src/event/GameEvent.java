/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package event;

import core.GridMap;
import game.GameEngine;

/**
 * <p>Abstract base class for timed game events with a cooldown timer and lifecycle management.</p>
 */
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
