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
        arrowShoot = engine.loadAudio("sounds/arrow_shoot.wav");
        cannonShoot = engine.loadAudio("sounds/cannon_shoot.wav");
        iceShoot = engine.loadAudio("sounds/ice_shoot.wav");
        lightningShoot = engine.loadAudio("sounds/lightning_shoot.wav");
        enemyDeath = engine.loadAudio("sounds/enemy_death.wav");
        baseHit = engine.loadAudio("sounds/base_hit.wav");
        buttonClick = engine.loadAudio("sounds/button_click.wav");
        placeBuilding = engine.loadAudio("sounds/place_building.wav");
        wallBreak = engine.loadAudio("sounds/wall_break.wav");
        healTower = engine.loadAudio("sounds/heal_tower.wav");
        decoyDeploy = engine.loadAudio("sounds/decoy_deploy.wav");
        rewardCollect = engine.loadAudio("sounds/reward_collect.wav");
        waveStart = engine.loadAudio("sounds/wave_start.wav");
        enemySpawn = engine.loadAudio("sounds/enemy_spawn.wav");
        fireDisaster = engine.loadAudio("sounds/fire_disaster.wav");
        meteorDisaster = engine.loadAudio("sounds/meteor_disaster.wav");
        insufficientMoney = engine.loadAudio("sounds/insufficient_money.wav");
        gameOver = engine.loadAudio("sounds/game_over.wav");
        gameWin = engine.loadAudio("sounds/game_win.wav");
        bgm = engine.loadAudio("sounds/bgm.wav");
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

    private void play(GameEngine.AudioClip clip, float volume) {
        if (clip != null && !muted) {
            engine.playAudio(clip, volume);
        }
    }

    public void playArrowShoot() { play(arrowShoot); }
    public void playCannonShoot() { play(cannonShoot); }
    public void playIceShoot() { play(iceShoot); }
    public void playLightningShoot() { play(lightningShoot); }
    public void playEnemyDeath() { play(enemyDeath); }
    public void playBaseHit() { play(baseHit); }
    public void playButtonClick() { play(buttonClick, -5.0f); }
    public void playPlaceBuilding() { play(placeBuilding); }
    public void playWallBreak() { play(wallBreak); }
    public void playHealTower() { play(healTower); }
    public void playDecoyDeploy() { play(decoyDeploy); }
    public void playRewardCollect() { play(rewardCollect); }
    public void playWaveStart() { play(waveStart, -6.0f); }
    public void playEnemySpawn() { play(enemySpawn, -6.0f); }
    public void playFireDisaster() { play(fireDisaster); }
    public void playMeteorDisaster() { play(meteorDisaster); }
    public void playInsufficientMoney() { play(insufficientMoney); }
    public void playGameOver() { play(gameOver); }
    public void playGameWin() { play(gameWin); }
    public void startBgm() { if (!muted && bgm != null) engine.startAudioLoop(bgm, BGM_VOLUME); }
    public void stopBgm() { if (bgm != null) engine.stopAudioLoop(bgm); }
}
