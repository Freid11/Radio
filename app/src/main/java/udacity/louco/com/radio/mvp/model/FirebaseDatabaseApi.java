package udacity.louco.com.radio.mvp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import udacity.louco.com.radio.BuildConfig;

public class FirebaseDatabaseApi {

    private static FirebaseDatabaseApi firebaseDatabaseApi;

    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReference;

    private final String mDataBaseName = BuildConfig.NameBase;

    private FirebaseDatabaseApi() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference().child(mDataBaseName);
    }

    public static FirebaseDatabaseApi getInstance(){
        if(firebaseDatabaseApi == null){
            firebaseDatabaseApi = new FirebaseDatabaseApi();
        }

        return firebaseDatabaseApi;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
