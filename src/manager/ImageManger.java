package manager;

import java.awt.*;
import java.awt.image.BufferedImage;

import enemy.EnemyType;
import game.GameEngine;

/** Loads image assets and serves animation frames. */
public class ImageManger {
    private static final int ENEMY_FRAME_SIZE = 256;
    private static final int ENEMY_FRAME_INSET = 14;
    private static final int ENEMY_SHEET_COLUMNS = 8;
    private static final int ENEMY_RUN_FRAME_COUNT = 16;
    private static final int ENEMY_ATTACK_FRAME_COUNT = 8;

    GameEngine engine;
    private static Image arrowTower;
    private static Image attackTower;
    private static Image healTower;
    private static Image iceTower;
    private static Image lightningTower;
    private static Image wall;
    private static Image bait;
    private static Image coin;
    private static Image[] coinTurnFrames;
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

    private static Image[] mapTiles;
    private static Image[] obstacleSprites;
    private static Image[] fireAnimationFrames;

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
    private static Image[] meleeEnemyRunLeft;
    private static Image[] meleeEnemyAttack;
    private static Image[] meleeEnemyAttackLeft;
    private static Image[] tankEnemyRun;
    private static Image[] tankEnemyRunLeft;
    private static Image[] tankEnemyAttack;
    private static Image[] tankEnemyAttackLeft;
    private static Image[] assassinEnemyRun;
    private static Image[] assassinEnemyRunLeft;
    private static Image[] assassinEnemyAttack;
    private static Image[] assassinEnemyAttackLeft;
    private static Image[] archerEnemyRun;
    private static Image[] archerEnemyRunLeft;
    private static Image[] archerEnemyAttack;
    private static Image[] archerEnemyAttackLeft;
    private static Image[] bossEnemyRun;
    private static Image[] bossEnemyRunLeft;
    private static Image[] bossEnemyAttack;
    private static Image[] bossEnemyAttackLeft;

    public void loadImages(GameEngine engine) {
        this.engine = engine;
        loadStaticImages(engine);
        loadEnemySprites(engine);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        return getEnemySprite(type, animationTime, false);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime, boolean facingLeft) {
        Image[] frames = getEnemyRunFrames(type);
        if (facingLeft) {
            frames = getEnemyRunLeftFrames(type);
        }
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime) {
        return getEnemyAttackSprite(type, animationTime, false);
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime, boolean facingLeft) {
        Image[] frames = getEnemyAttackFrames(type);
        if (facingLeft) {
            frames = getEnemyAttackLeftFrames(type);
        }
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    private static Image[] getEnemyRunFrames(EnemyType type) {
        if (type == EnemyType.BOSS) {
            return bossEnemyRun;
        }
        if (type == EnemyType.ELITE) {
            return tankEnemyRun;
        }
        if (type == EnemyType.TANK) {
            return tankEnemyRun;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyRun;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyRun;
        }
        return meleeEnemyRun;
    }

    private static Image[] getEnemyRunLeftFrames(EnemyType type) {
        if (type == EnemyType.BOSS) {
            return bossEnemyRunLeft;
        }
        if (type == EnemyType.ELITE) {
            return tankEnemyRunLeft;
        }
        if (type == EnemyType.TANK) {
            return tankEnemyRunLeft;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyRunLeft;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyRunLeft;
        }
        return meleeEnemyRunLeft;
    }

    private static Image[] getEnemyAttackFrames(EnemyType type) {
        if (type == EnemyType.BOSS) {
            return bossEnemyAttack;
        }
        if (type == EnemyType.ELITE) {
            return tankEnemyAttack;
        }
        if (type == EnemyType.TANK) {
            return tankEnemyAttack;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyAttack;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyAttack;
        }
        return meleeEnemyAttack;
    }

    private static Image[] getEnemyAttackLeftFrames(EnemyType type) {
        if (type == EnemyType.BOSS) {
            return bossEnemyAttackLeft;
        }
        if (type == EnemyType.ELITE) {
            return tankEnemyAttackLeft;
        }
        if (type == EnemyType.TANK) {
            return tankEnemyAttackLeft;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyAttackLeft;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyAttackLeft;
        }
        return meleeEnemyAttackLeft;
    }

    private void loadStaticImages(GameEngine engine) {
        arrowTower = engine.loadImage("Images/arrowTower.png");
        attackTower = engine.loadImage("Images/attackTower.png");
        healTower = engine.loadImage("Images/healTower.png");
        iceTower = engine.loadImage("Images/iceTower.png");
        lightningTower = engine.loadImage("Images/lightningTower.png");
        wall = engine.loadImage("Images/wall.png");
        bait = engine.loadImage("Images/bait.png");
        coin = engine.loadImage("Images/coin.png");
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

        loadMapTiles(engine);
        loadObstacleSprites(engine);
        loadFireAnimationFrames(engine);
        loadCoinTurnFrames(engine);
    }

    private void loadEnemySprites(GameEngine engine) {
        Image meleeSheet = engine.loadImage("Images/meleeEnemy.png");
        meleeEnemyRun = loadEnemyRunSprite(engine, meleeSheet);
        meleeEnemyRunLeft = flipFrames(meleeEnemyRun);
        meleeEnemyAttack = loadEnemyAttackSprite(engine, meleeSheet);
        meleeEnemyAttackLeft = flipFrames(meleeEnemyAttack);

        Image tankSheet = engine.loadImage("Images/tankEnemy.png");
        tankEnemyRun = loadEnemyRunSprite(engine, tankSheet);
        tankEnemyRunLeft = flipFrames(tankEnemyRun);
        tankEnemyAttack = loadEnemyAttackSprite(engine, tankSheet);
        tankEnemyAttackLeft = flipFrames(tankEnemyAttack);

        Image assassinSheet = engine.loadImage("Images/assassinEnemy.png");
        assassinEnemyRun = loadEnemyRunSprite(engine, assassinSheet);
        assassinEnemyRunLeft = flipFrames(assassinEnemyRun);
        assassinEnemyAttack = loadEnemyAttackSprite(engine, assassinSheet);
        assassinEnemyAttackLeft = flipFrames(assassinEnemyAttack);

        Image archerSheet = engine.loadImage("Images/archerEnemy.png");
        archerEnemyRun = loadEnemyRunSprite(engine, archerSheet);
        archerEnemyRunLeft = flipFrames(archerEnemyRun);
        archerEnemyAttack = loadEnemyAttackSprite(engine, archerSheet);
        archerEnemyAttackLeft = flipFrames(archerEnemyAttack);

        Image bossSheet = engine.loadImage("Images/bossEnemy.png");
        bossEnemyRun = loadEnemyRunSprite(engine, bossSheet);
        bossEnemyRunLeft = flipFrames(bossEnemyRun);
        bossEnemyAttack = loadEnemyAttackSprite(engine, bossSheet);
        bossEnemyAttackLeft = flipFrames(bossEnemyAttack);
    }

    private Image[] loadEnemyRunSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_RUN_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            int row = i / ENEMY_SHEET_COLUMNS;
            int col = i % ENEMY_SHEET_COLUMNS;
            frames[i] = getEnemyFrame(engine, sheet, row, col);
        }
        return frames;
    }

    private Image[] loadEnemyAttackSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_ATTACK_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = getEnemyFrame(engine, sheet, 2, i);
        }
        return frames;
    }

    private Image getEnemyFrame(GameEngine engine, Image sheet, int row, int col) {
        int x = col * ENEMY_FRAME_SIZE + ENEMY_FRAME_INSET;
        int y = row * ENEMY_FRAME_SIZE + ENEMY_FRAME_INSET;
        int size = ENEMY_FRAME_SIZE - ENEMY_FRAME_INSET * 2;
        return engine.subImage(sheet, x, y, size, size);
    }

    private Image[] flipFrames(Image[] frames) {
        Image[] flipped = new Image[frames.length];
        for (int i = 0; i < frames.length; i++) {
            flipped[i] = flipImage(frames[i]);
        }
        return flipped;
    }

    private Image flipImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = flipped.createGraphics();
        graphics.drawImage(image, width, 0, -width, height, null);
        graphics.dispose();
        return flipped;
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

    public static Image getWall() {
        return wall;
    }

    public static Image getBait() {
        return bait;
    }

    public static Image getCoin() {
        if (coinTurnFrames != null && coinTurnFrames.length > 0) {
            return coinTurnFrames[0];
        }
        return coin;
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

    public static Image getMapTile(int index) {
        if (mapTiles == null || index < 0 || index >= mapTiles.length) {
            return null;
        }
        return mapTiles[index];
    }

    public static int getMapTileCount() {
        return mapTiles != null ? mapTiles.length : 0;
    }

    public static Image getObstacleSprite(int index) {
        if (obstacleSprites == null || index < 0 || index >= obstacleSprites.length) {
            return null;
        }
        return obstacleSprites[index];
    }

    public static Image getFireAnimationFrame(double animationTime) {
        if (fireAnimationFrames == null || fireAnimationFrames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 4)) % fireAnimationFrames.length;
        return fireAnimationFrames[frameIndex];
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

    private void loadMapTiles(GameEngine engine) {
        mapTiles = new Image[3];
        mapTiles[0] = engine.loadImage("Images/mapGround.png");
        mapTiles[1] = engine.loadImage("Images/mapGross.png");
        mapTiles[2] = engine.loadImage("Images/mapFlower.png");
    }

    private void loadObstacleSprites(GameEngine engine) {
        obstacleSprites = new Image[4];
        for (int i = 0; i < 4; i++) {
            obstacleSprites[i] = engine.loadImage("Images/tree_" + (i + 1) + ".png");
        }
    }

    private void loadFireAnimationFrames(GameEngine engine) {
        Image fireSheet = engine.loadImage("Images/fireAnimation.png");
        int frameWidth = 44;
        int frameHeight = 48;
        int column = 2;
        int rows = 6;
        fireAnimationFrames = new Image[rows];
        for (int i = 0; i < rows; i++) {
            fireAnimationFrames[i] = engine.subImage(fireSheet, column * frameWidth, i * frameHeight, frameWidth, frameHeight);
        }
    }

    private void loadCoinTurnFrames(GameEngine engine) {
        Image coinSheet = engine.loadImage("Images/coinTurn.png");
        int columns = 8;
        int rows = 1;
        int totalFrames = columns * rows;
        coinTurnFrames = new Image[totalFrames];

        int frameWidth = coinSheet.getWidth(null) / columns;
        int frameHeight = coinSheet.getHeight(null) / rows;

        for (int i = 0; i < totalFrames; i++) {
            int col = i % columns;
            coinTurnFrames[i] = engine.subImage(coinSheet, col * frameWidth, 0, frameWidth, frameHeight);
        }
    }

    public static Image getCoinTurnFrame(double animationTime) {
        if (coinTurnFrames == null || coinTurnFrames.length == 0) {
            return coin;
        }
        int frameIndex = ((int) (animationTime * 10)) % coinTurnFrames.length;
        return coinTurnFrames[frameIndex];
    }
}
