package manager;

import game.GameEngine;

/** Central sound facade. Singleton so any gameplay class can call it. */
public class SoundManager {
    private static SoundManager instance;

    private static final float SFX_VOLUME = -3.0f;
    private static final float BGM_VOLUME = -12.0f;

    private GameEngine engine;
    private boolean muted;

    private GameEngine.AudioClip arrowShoot;
    private GameEngine.AudioClip cannonShoot;
    private GameEngine.AudioClip iceShoot;
    private GameEngine.AudioClip lightningShoot;
    private GameEngine.AudioClip enemyDeath;
    private GameEngine.AudioClip baseHit;
    private GameEngine.AudioClip buttonClick;
    private GameEngine.AudioClip placeBuilding;
    private GameEngine.AudioClip wallBreak;
    private GameEngine.AudioClip healTower;
    private GameEngine.AudioClip decoyDeploy;
    private GameEngine.AudioClip rewardCollect;
    private GameEngine.AudioClip waveStart;
    private GameEngine.AudioClip enemySpawn;
    private GameEngine.AudioClip fireDisaster;
    private GameEngine.AudioClip meteorDisaster;
    private GameEngine.AudioClip insufficientMoney;
    private GameEngine.AudioClip gameOver;
    private GameEngine.AudioClip gameWin;
    private GameEngine.AudioClip bgm;

    private SoundManager(GameEngine engine) {
        this.engine = engine;
    }

    public static void init(GameEngine engine) {
        instance = new SoundManager(engine);
    }

    public static SoundManager getInstance() {
        return instance;
    }

    public void loadSounds() {
        arrowShoot = tryLoad("sounds/arrow_shoot.wav");
        cannonShoot = tryLoad("sounds/cannon_shoot.wav");
        iceShoot = tryLoad("sounds/ice_shoot.wav");
        lightningShoot = tryLoad("sounds/lightning_shoot.wav");
        enemyDeath = tryLoad("sounds/enemy_death.wav");
        baseHit = tryLoad("sounds/base_hit.wav");
        buttonClick = tryLoad("sounds/button_click.wav");
        placeBuilding = tryLoad("sounds/place_building.wav");
        wallBreak = tryLoad("sounds/wall_break.wav");
        healTower = tryLoad("sounds/heal_tower.wav");
        decoyDeploy = tryLoad("sounds/decoy_deploy.wav");
        rewardCollect = tryLoad("sounds/reward_collect.wav");
        waveStart = tryLoad("sounds/wave_start.wav");
        enemySpawn = tryLoad("sounds/enemy_spawn.wav");
        fireDisaster = tryLoad("sounds/fire_disaster.wav");
        meteorDisaster = tryLoad("sounds/meteor_disaster.wav");
        insufficientMoney = tryLoad("sounds/insufficient_money.wav");
        gameOver = tryLoad("sounds/game_over.wav");
        gameWin = tryLoad("sounds/game_win.wav");
        bgm = tryLoad("sounds/bgm.wav");
    }

    private GameEngine.AudioClip tryLoad(String path) {
        return engine.loadAudio(path);
    }

    public void toggleMute() {
        muted = !muted;
        if (muted) {
            if (bgm != null) engine.stopAudioLoop(bgm);
        } else {
            if (bgm != null) engine.startAudioLoop(bgm, BGM_VOLUME);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    private void play(GameEngine.AudioClip clip) {
        play(clip, SFX_VOLUME);
    }

    private long lastPlayTime;
    private static final long GLOBAL_COOLDOWN = 50_000_000L; // 50ms global throttle
    private static final long COOLDOWN = 250_000_000L; // 250ms per sound

    private void play(GameEngine.AudioClip clip, float volume) {
        if (clip != null && !muted) {
            long now = System.nanoTime();
            if (now - lastPlayTime < GLOBAL_COOLDOWN) return;
            lastPlayTime = now;
            engine.playAudio(clip, volume);
        }
    }

    private long lastArrowTime, lastCannonTime, lastIceTime, lastLightningTime;
    private long lastDeathTime, lastBaseHitTime, lastWallTime, lastSpawnTime;

    public void playArrowShoot() {
        long now = System.nanoTime();
        if (now - lastArrowTime >= COOLDOWN) { lastArrowTime = now; play(arrowShoot); }
    }
    public void playCannonShoot() {
        long now = System.nanoTime();
        if (now - lastCannonTime >= COOLDOWN) { lastCannonTime = now; play(cannonShoot); }
    }
    public void playIceShoot() {
        long now = System.nanoTime();
        if (now - lastIceTime >= COOLDOWN) { lastIceTime = now; play(iceShoot); }
    }
    public void playLightningShoot() {
        long now = System.nanoTime();
        if (now - lastLightningTime >= COOLDOWN) { lastLightningTime = now; play(lightningShoot); }
    }
    public void playEnemyDeath() {
        long now = System.nanoTime();
        if (now - lastDeathTime >= COOLDOWN) { lastDeathTime = now; play(enemyDeath); }
    }
    public void playBaseHit() {
        long now = System.nanoTime();
        if (now - lastBaseHitTime >= COOLDOWN) { lastBaseHitTime = now; play(baseHit); }
    }
    public void playButtonClick() { play(buttonClick, -5.0f); }
    public void playPlaceBuilding() { play(placeBuilding); }
    public void playWallBreak() {
        long now = System.nanoTime();
        if (now - lastWallTime >= COOLDOWN) { lastWallTime = now; play(wallBreak); }
    }
    public void playHealTower() { play(healTower); }
    public void playDecoyDeploy() { play(decoyDeploy); }
    public void playRewardCollect() { play(rewardCollect); }
    public void playWaveStart() { play(waveStart); }
    public void playEnemySpawn() {
        long now = System.nanoTime();
        if (now - lastSpawnTime >= COOLDOWN) { lastSpawnTime = now; play(enemySpawn, -6.0f); }
    }
    public void playFireDisaster() { play(fireDisaster); }
    public void playMeteorDisaster() { play(meteorDisaster); }
    public void playInsufficientMoney() { play(insufficientMoney); }
    public void playGameOver() { play(gameOver); }
    public void playGameWin() { play(gameWin); }
    public void startBgm() { if (!muted && bgm != null) engine.startAudioLoop(bgm, BGM_VOLUME); }
    public void stopBgm() { if (bgm != null) engine.stopAudioLoop(bgm); }
}
