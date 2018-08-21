package udacity.louco.com.radio.mvp.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;

import io.reactivex.subjects.BehaviorSubject;

public class Player {

    private static ExoPlayer exoPlayer;
    private static TrackRenderer audioRenderer;
    public static BehaviorSubject<Integer> sState = BehaviorSubject.create();

    public static void start(String URL, Context context) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        Uri URI = Uri.parse(URL);

        FrameworkSampleSource sampleSource = new FrameworkSampleSource(context, URI, null);
        audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, null, true);
        exoPlayer = ExoPlayer.Factory.newInstance(1);
        exoPlayer.prepare(audioRenderer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(new ExoPlayer.Listener() {


            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                // This state if player is ready to work and loaded all data
                sState.onNext(playbackState);
            }

            @Override
            public void onPlayWhenReadyCommitted() {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("Louco", error.getMessage());
            }

        });
    }

    public static void stop() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
    }

    public static void setVolume(float volume) {
        if (exoPlayer != null) {
            exoPlayer.sendMessage(audioRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, volume);
        }
    }
}
