package udacity.louco.com.radio.component;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import io.reactivex.observers.DisposableObserver;
import udacity.louco.com.radio.mvp.model.Player;
import udacity.louco.com.radio.mvp.model.object.Radio;

public class PlayerService extends Service{

    private Radio lastRadio;
    public static final int PLAY_RADIO = 11;

    public PlayerService() {
        Log.d("Louco", "PlayerService");
        startRadio();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Louco", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    private void startRadio() {
        subscribeRadio();
        subscribeState();
    }

    private void subscribeRadio() {
        Radio.sRadio
                .subscribe(new DisposableObserver<Radio>() {
                    @Override
                    public void onNext(Radio radio) {
                        if (radio != null) {
                            if (lastRadio == null || !radio.equals(lastRadio)) {
                                lastRadio = radio;
                                Player.start(lastRadio.getUrl_audio(), getBaseContext());
                            } else {
                                Log.e("Louco", "lastUrl == url");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void subscribeState(){
        Player.sState.subscribe(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer playbackState) {
                Log.d("Louco", String.valueOf(playbackState));
                if(playbackState == PLAY_RADIO){
                    Player.start(lastRadio.getUrl_audio(), getBaseContext());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
