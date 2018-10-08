package udacity.louco.com.radio.mvp.presenter;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.exoplayer.ExoPlayer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import udacity.louco.com.radio.R;
import udacity.louco.com.radio.component.PlayerService;
import udacity.louco.com.radio.mvp.model.FirebaseDatabaseApi;
import udacity.louco.com.radio.mvp.model.Player;
import udacity.louco.com.radio.mvp.model.db.ContentDao;
import udacity.louco.com.radio.mvp.model.db.RadioDatabase;
import udacity.louco.com.radio.mvp.model.object.Radio;
import udacity.louco.com.radio.mvp.view.RadioListView;

@InjectViewState
public class RadioListPresenter extends MvpPresenter<RadioListView> {

    private boolean isShowFavoriteList = false;

    private ContentDao mContentDao;

    private List<Radio> mListRadioFavorite;
    private List<Radio> mListRadioDataBase = new ArrayList<>();

    public RadioListPresenter() {
        FirebaseDatabaseApi mFirebaseDatabaseApi = FirebaseDatabaseApi.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabaseApi.getDatabaseReference();

        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showRadio(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Louco", databaseError.getMessage());
            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);

        Radio.sRadio.subscribe(new DisposableObserver<Radio>() {
            @Override
            public void onNext(Radio radio) {
                getViewState().showInfoRadio(radio);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Player.sState.subscribe(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                getViewState().showPlayerState(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getInstanceDatabase(Context context) {
        RadioDatabase mRadioDatabase = RadioDatabase.getInstance(context);
        mContentDao = mRadioDatabase.getContent();

        LiveData<List<Radio>> listLiveData = mContentDao.getFavorite();
        listLiveData.observe((LifecycleOwner) context, radios -> {
            mListRadioFavorite = radios;

            if (isShowFavoriteList) {
                getViewState().showsRadio(mListRadioFavorite);
            }
        });
    }

    private void showRadio(@NonNull DataSnapshot dataSnapshot) {
        Radio radio = dataSnapshot.getValue(Radio.class);
        if (radio != null) {
            mListRadioDataBase.add(radio);
            getViewState().addRadio(radio);
        }
    }

    public void startService(Activity activity) {
        Intent intent = new Intent(activity, PlayerService.class);
        activity.startService(intent);
    }

    public void onClickRadio(Radio radio) {
        if (mListRadioFavorite.contains(radio)) {
            radio.setLike(true);
        } else {
            radio.setLike(false);
        }
        Radio.sRadio.onNext(radio);
    }

    public void switchFavorite() {
        Radio radio = Radio.sRadio.getValue();
        if (radio != null) {
            Radio radioSearch = searchRadioInDatabase(radio);
            if (radio.isLike()) {
                if (radioSearch != null)
                    mContentDao.delete(radioSearch);
            } else {
                if (radioSearch == null)
                    mContentDao.insert(radio);
            }
            radio.switchLike();
            getViewState().showFavoriteState(radio);
        }
    }

    private Radio searchRadioInDatabase(Radio radio) {
        for (Radio radioFavorite : mListRadioFavorite) {
            if (radioFavorite.equals(radio)) {
                return radioFavorite;
            }
        }
        return null;
    }

    public void clickPlayPlayer() {
        Integer state = Player.sState.getValue();
        if (state != null) {
            if (Player.sState.getValue() == ExoPlayer.STATE_READY) {
                Player.stop();
            } else {
                Player.sState.onNext(PlayerService.PLAY_RADIO);
            }
        }
    }

    public void switchMenu(int itemId) {
        switch (itemId) {
            case R.id.item_radio:
                getViewState().showsRadio(mListRadioDataBase);
                isShowFavoriteList = false;
                break;
            case R.id.item_favorite:
                getViewState().showsRadio(mListRadioFavorite);
                isShowFavoriteList = true;
                break;
        }
    }
}

