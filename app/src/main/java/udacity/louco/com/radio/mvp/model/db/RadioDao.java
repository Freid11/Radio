package udacity.louco.com.radio.mvp.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import udacity.louco.com.radio.mvp.model.object.Radio;

@Dao
public interface RadioDao {

    @Query("SELECT * FROM radio")
    LiveData<List<Radio>> getFavorite();

    @Query("SELECT * FROM radio")
    List<Radio> getFavoriteNotLiveData();

    @Query("SELECT * FROM radio WHERE name = :radioName")
    Radio getRadio(String radioName);

    @Delete
    void delete(Radio radio);

    @Insert
    void insert(Radio radio);

}
