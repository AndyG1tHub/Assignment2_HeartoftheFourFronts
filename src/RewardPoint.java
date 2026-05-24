import java.awt.Color;

/** Clickable timed pickup that grants money and score. */
public class RewardPoint extends GameEvent {
    private final GridPosition position;

    public RewardPoint(GridPosition position) {
        super(EventType.REWARD_POINT, 8.0);
        this.position = position;
    }

    public boolean isClicked(GridPosition clicked) {
        return active && position.equals(clicked);
    }

    public void collect(EconomyManager economy, ScoreManager score) {
        economy.addMoney(GameConfig.REWARD_MONEY);
        score.addRewardScore(GameConfig.REWARD_SCORE);
        active = false;
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playRewardCollect();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        engine.changeColor(Color.YELLOW);
        engine.drawSolidCircle(map.tileCenterX(position), map.tileCenterY(position), 8);
    }
}
