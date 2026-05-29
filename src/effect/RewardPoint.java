package effect;

import java.awt.Color;
import java.awt.Image;

import core.GridPosition;
import core.GridMap;
import event.GameEvent;
import event.EventType;
import game.GameEngine;
import game.GameConfig;
import manager.EconomyManager;
import manager.ImageManger;
import manager.ScoreManager;
import manager.SoundManager;

/** Clickable timed pickup that grants money and score. */
public class RewardPoint extends GameEvent {
    private final GridPosition position;
    private double animationTime = 0;

    public RewardPoint(GridPosition position) {
        super(EventType.REWARD_POINT, GameConfig.REWARD_DURATION);
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
    public void update(double dt) {
        super.update(dt);
        animationTime += dt;
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        Image coinImage = ImageManger.getCoinTurnFrame(animationTime);
        int centerX = map.tileCenterX(position);
        int centerY = map.tileCenterY(position);
        int size = 26;
        if (coinImage != null) {
            engine.drawImage(coinImage, centerX - size / 2, centerY - size / 2, size, size);
        } else {
            engine.changeColor(Color.YELLOW);
            engine.drawSolidCircle(centerX, centerY, 8);
        }
    }
}
