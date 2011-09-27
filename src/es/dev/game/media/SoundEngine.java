package es.dev.game.media;

import es.dev.game.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Cares about everything regarding sound and music.
 *
 */
public class SoundEngine {

    MediaPlayer ballBounceSound;
    MediaPlayer bgMusic;

    private SoundPool soundPool;
    private int SOUNDID_BALL;

    public SoundEngine(Context ctx) {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        SOUNDID_BALL = soundPool.load(ctx, R.raw.background_music_aac  , 1);
    }

    public void playBallBounceSound() {
        soundPool.play(SOUNDID_BALL, 1f, 1f, 0, 0, 1.0f);
    }

    public void playBGMusic() {
    }
}