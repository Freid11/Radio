package udacity.louco.com.radio.mvp.model.db;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import udacity.louco.com.radio.mvp.model.object.Radio;

public class ContentDao implements RadioDao{

    private RadioDao radioDao;

    public ContentDao(RadioDao radioDao) {
        this.radioDao = radioDao;
    }

    @Override
    public LiveData<List<Radio>> getFavorite() {
        return radioDao.getFavorite();
    }

    @Override
    public List<Radio> getFavoriteNotLiveData() {
        return radioDao.getFavoriteNotLiveData();
    }

    @Override
    public Radio getRadio(String radioName) {
        return radioDao.getRadio(radioName);
    }

    @SuppressLint("CheckResult")
    @Override
    public void delete(Radio radio) {
        Log.d("Louco", "delete");
        Single.just(radio)
                .observeOn(Schedulers.io())
                .subscribe(radioDao::delete);
    }

    @SuppressLint("CheckResult")
    @Override
    public void insert(Radio radio) {
        Log.d("Louco", "insert");
        Single.just(radio)
                .observeOn(Schedulers.io())
                .subscribe(radioDao::insert);
    }
}
