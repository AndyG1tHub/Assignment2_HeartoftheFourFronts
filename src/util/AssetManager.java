package util;

import java.awt.Image;
import java.util.EnumMap;
import java.util.Map;

import enemy.EnemyType;
import game.GameEngine;

/** Loads and serves shared sprite assets. */
public class AssetManager {
    private static final int ENEMY_FRAME_COUNT = 4;
    private static final Map<EnemyType, Image[]> enemySprites = new EnumMap<EnemyType, Image[]>(EnemyType.class);

    public void loadAssets(GameEngine engine) {
        loadEnemySprites(engine);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        Image[] frames = enemySprites.get(type);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    private void loadEnemySprites(GameEngine engine) {
        loadEnemySprite(engine, EnemyType.MELEE, "Images/meleeEnemy.png");
        loadEnemySprite(engine, EnemyType.TANK, "Images/tankEnemy.png");
        loadEnemySprite(engine, EnemyType.ASSASSIN, "Images/assassinEnemy.png");
        loadEnemySprite(engine, EnemyType.ARCHER, "Images/archerEnemy.png");
        loadEnemySprite(engine, EnemyType.HEALER, "Images/healerEnemy.png");
    }

    private void loadEnemySprite(GameEngine engine, EnemyType type, String filename) {
        Image sheet = engine.loadImage(filename);
        int frameWidth = sheet.getWidth(null) / ENEMY_FRAME_COUNT;
        int frameHeight = sheet.getHeight(null);
        Image[] frames = new Image[ENEMY_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = engine.subImage(sheet, i * frameWidth, 0, frameWidth, frameHeight);
        }
        enemySprites.put(type, frames);
    }
}
