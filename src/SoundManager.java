/** Placeholder sound facade so audio can be added without touching gameplay classes. */
public class SoundManager {
    private boolean muted;

    public void toggleMute() {
        muted = !muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void playClick() {
        // TODO: play UI click sound when audio files exist.
    }
}
