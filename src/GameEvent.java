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
