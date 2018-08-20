package udacity.louco.com.radio.mvp.model.object;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import io.reactivex.subjects.BehaviorSubject;

@Entity
public class Radio implements Parcelable {

    public static final String KEY_BUNDLE = "radio_key";

    @Ignore
    public static BehaviorSubject<Radio> sRadio = BehaviorSubject.create();

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String url_audio;
    private String url_image;
    private Boolean like = false;

    public Radio(int id, String name, String url_audio, String url_image, Boolean like) {
        this.id = id;
        this.name = name;
        this.url_audio = url_audio;
        this.url_image = url_image;
        this.like = like;
    }

    public Radio() {
    }

    protected Radio(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url_audio = in.readString();
        url_image = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl_audio() {
        return url_audio;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl_audio(String url_audio) {
        this.url_audio = url_audio;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean isLike() {
        return like;
    }

    public void switchLike(){
        like = !like;
    }

    public static final Creator<Radio> CREATOR = new Creator<Radio>() {
        @Override
        public Radio createFromParcel(Parcel in) {
            return new Radio(in);
        }

        @Override
        public Radio[] newArray(int size) {
            return new Radio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url_audio);
        dest.writeString(url_image);
    }

    @Override
    public String toString() {
        return "Radio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url_audio='" + url_audio + '\'' +
                ", url_image='" + url_image + '\'' +
                ", like=" + like +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Radio)) return false;
        Radio radio = (Radio) o;
        return Objects.equals(name, radio.name) &&
                Objects.equals(url_audio, radio.url_audio) &&
                Objects.equals(url_image, radio.url_image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url_audio, url_image);
    }
}
