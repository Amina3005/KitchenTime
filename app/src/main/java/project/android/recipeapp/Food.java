package project.android.recipeapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String image;
    private String title;
    private int id;

    Food(int id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    protected Food(Parcel in) {
        image = in.readString();
        title = in.readString();
        id = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public  int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(title);
    }
}
