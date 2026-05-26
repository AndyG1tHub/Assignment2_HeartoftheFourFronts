package manager;

import java.awt.*;

import enemy.EnemyType;
import game.GameEngine;

/** Loads image assets and serves animation frames. */
public class ImageManger {
    private static final int ENEMY_FRAME_SIZE = 256;
    private static final int ENEMY_SHEET_COLUMNS = 8;
    private static final int ENEMY_RUN_FRAME_COUNT = 16;
    private static final int ENEMY_ATTACK_FRAME_COUNT = 8;

    GameEngine engine;
    private static Image arrowTower;
    private static Image attackTower;
    private static Image healTower;
    private static Image iceTower;
    private static Image lightningTower;
    Image wall;
    private static Image bait;
    Image homeItems;
    Image enemies;
    Image naturalDisaster;
    private static Image baseImage;
    private static Image arrowProjectile;
    private static Image cannonProjectile;
    private static Image iceProjectile;
    private static Image laserEffect;
    private static Image healRangeEffect;
    private static Image explosionFireball;

    private static Image uiSheet;
    private static Image btnPlayHover;
    private static Image btnPlayNormal;
    private static Image btnExitHover;
    private static Image btnExitNormal;
    private static Image btnPlainNormal;
    private static Image btnPlainHover;
    private static Image panelTL, panelTR, panelBL, panelBR;
    private static Image pauseTL, pauseTR, pauseBL, pauseBR;

    private static Image[] meleeEnemyRun;
    private static Image[] meleeEnemyAttack;
    private static Image[] tankEnemyRun;
    private static Image[] tankEnemyAttack;
    private static Image[] assassinEnemyRun;
    private static Image[] assassinEnemyAttack;
    private static Image[] archerEnemyRun;
    private static Image[] archerEnemyAttack;
    private static Image[] healerEnemyRun;
    private static Image[] healerEnemyAttack;

    public void loadImages(GameEngine engine) {
        this.engine = engine;
        loadStaticImages(engine);
        loadEnemySprites(engine);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        Image[] frames = getEnemyRunFrames(type);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime) {
        Image[] frames = getEnemyAttackFrames(type);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    private static Image[] getEnemyRunFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyRun;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyRun;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyRun;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyRun;
        }
        return meleeEnemyRun;
    }

    private static Image[] getEnemyAttackFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyAttack;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyAttack;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyAttack;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyAttack;
        }
        return meleeEnemyAttack;
    }

    private void loadStaticImages(GameEngine engine) {
        arrowTower = engine.loadImage("Images/arrowTower.png");
        attackTower = engine.loadImage("Images/attackTower.png");
        healTower = engine.loadImage("Images/healTower.png");
        iceTower = engine.loadImage("Images/iceTower.png");
        lightningTower = engine.loadImage("Images/lightningTower.png");
        wall = engine.loadImage("Images/wall.png");
        bait = engine.loadImage("Images/bait.png");
        homeItems = engine.loadImage("Images/homeItems.png");
        naturalDisaster = engine.loadImage("Images/naturalDisaster.png");
        baseImage = engine.subImage(homeItems, 45, 80, 470, 500);
        arrowProjectile = engine.subImage(homeItems, 575, 310, 250, 175);
        cannonProjectile = engine.subImage(homeItems, 895, 330, 245, 170);
        iceProjectile = engine.subImage(homeItems, 1180, 315, 265, 180);
        laserEffect = engine.subImage(homeItems, 170, 650, 300, 265);
        healRangeEffect = engine.subImage(homeItems, 555, 650, 340, 265);
        explosionFireball = engine.subImage(homeItems, 995, 630, 350, 285);

        uiSheet = engine.loadImage("Images/Ui.png");
        btnPlayHover = engine.subImage(uiSheet, 0, 128, 64, 32);
        btnPlayNormal = engine.subImage(uiSheet, 64, 128, 64, 32);
        btnExitHover = engine.subImage(uiSheet, 0, 192, 64, 32);
        btnExitNormal = engine.subImage(uiSheet, 64, 192, 64, 32);
        btnPlainHover = engine.subImage(uiSheet, 0, 224, 64, 32);
        btnPlainNormal = engine.subImage(uiSheet, 64, 224, 64, 32);
        panelTL = engine.subImage(uiSheet, 128, 0, 64, 64);
        panelTR = engine.subImage(uiSheet, 192, 0, 64, 64);
        panelBL = engine.subImage(uiSheet, 128, 64, 64, 64);
        panelBR = engine.subImage(uiSheet, 192, 64, 64, 64);
        pauseTL = engine.subImage(uiSheet, 128, 128, 64, 64);
        pauseTR = engine.subImage(uiSheet, 192, 128, 64, 64);
        pauseBL = engine.subImage(uiSheet, 128, 192, 64, 64);
        pauseBR = engine.subImage(uiSheet, 192, 192, 64, 64);
    }

    private void loadEnemySprites(GameEngine engine) {
        Image meleeSheet = engine.loadImage("Images/meleeEnemy.png");
        meleeEnemyRun = loadEnemyRunSprite(engine, meleeSheet);
        meleeEnemyAttack = loadEnemyAttackSprite(engine, meleeSheet);

        Image tankSheet = engine.loadImage("Images/tankEnemy.png");
        tankEnemyRun = loadEnemyRunSprite(engine, tankSheet);
        tankEnemyAttack = loadEnemyAttackSprite(engine, tankSheet);

        Image assassinSheet = engine.loadImage("Images/assassinEnemy.png");
        assassinEnemyRun = loadEnemyRunSprite(engine, assassinSheet);
        assassinEnemyAttack = loadEnemyAttackSprite(engine, assassinSheet);

        Image archerSheet = engine.loadImage("Images/archerEnemy.png");
        archerEnemyRun = loadEnemyRunSprite(engine, archerSheet);
        archerEnemyAttack = loadEnemyAttackSprite(engine, archerSheet);

        Image healerSheet = engine.loadImage("Images/healerEnemy.png");
        healerEnemyRun = loadEnemyRunSprite(engine, healerSheet);
        healerEnemyAttack = loadEnemyAttackSprite(engine, healerSheet);
    }

    private Image[] loadEnemyRunSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_RUN_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            int row = i / ENEMY_SHEET_COLUMNS;
            int col = i % ENEMY_SHEET_COLUMNS;
            frames[i] = engine.subImage(sheet, col * ENEMY_FRAME_SIZE, row * ENEMY_FRAME_SIZE,
                    ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE);
        }
        return frames;
    }

    private Image[] loadEnemyAttackSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_ATTACK_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = engine.subImage(sheet, i * ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE * 2,
                    ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE);
        }
        return frames;
    }

    public static Image getArrowTower() {
        return arrowTower;
    }

    public static Image getAttackTower() {
        return attackTower;
    }

    public static Image getHealTower() {
        return healTower;
    }

    public static Image getIceTower() {
        return iceTower;
    }

    public static Image getLightningTower() {
        return lightningTower;
    }

    public static Image getBait() {
        return bait;
    }

    public static Image getBaseImage() {
        return baseImage;
    }

    public static Image getArrowProjectile() {
        return arrowProjectile;
    }

    public static Image getCannonProjectile() {
        return cannonProjectile;
    }

    public static Image getIceProjectile() {
        return iceProjectile;
    }

    public static Image getLaserEffect() {
        return laserEffect;
    }

    public static Image getHealRangeEffect() {
        return healRangeEffect;
    }

    public static Image getExplosionFireball() {
        return explosionFireball;
    }

    public static Image getBtnPlayNormal() { return btnPlayNormal; }
    public static Image getBtnPlayHover() { return btnPlayHover; }
    public static Image getBtnExitNormal() { return btnExitNormal; }
    public static Image getBtnExitHover() { return btnExitHover; }
    public static Image getBtnPlainNormal() { return btnPlainNormal; }
    public static Image getBtnPlainHover() { return btnPlainHover; }
    public static Image getPanelTL() { return panelTL; }
    public static Image getPanelTR() { return panelTR; }
    public static Image getPanelBL() { return panelBL; }
    public static Image getPanelBR() { return panelBR; }
    public static Image getPauseTL() { return pauseTL; }
    public static Image getPauseTR() { return pauseTR; }
    public static Image getPauseBL() { return pauseBL; }
    public static Image getPauseBR() { return pauseBR; }
}
