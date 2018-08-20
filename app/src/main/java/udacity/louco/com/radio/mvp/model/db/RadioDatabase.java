package udacity.louco.com.radio.mvp.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import udacity.louco.com.radio.mvp.model.object.Radio;

@Database(entities = {Radio.class}, version = 1, exportSchema = false)
public abstract class RadioDatabase extends RoomDatabase{

    private static String RADIO_DATABASE = "radioData";
    private static final Object LOCK = new Object();
    private static RadioDatabase database;

    public static RadioDatabase getInstance(Context context){
        if(database == null){
            synchronized (LOCK){
                database = Room.databaseBuilder(context,
                        RadioDatabase.class, RADIO_DATABASE)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return database;
    }

    public ContentDao getContent(){
        return new ContentDao(getRadioDao());
    }

    public abstract RadioDao getRadioDao();
}
